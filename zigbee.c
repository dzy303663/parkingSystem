#include <stdio.h>
#include<stdlib.h>
#include<string.h>
#include <unistd.h>
#include <netinet/in.h>
#include<sys/socket.h>
#include <libwsncomm.h>	

typedef unsigned int uint16;
typedef unsigned char uint8;
static unsigned short port=8000;
WSNCOMM_NODE *wsncom_node;
WSNCOMM_NODE *node;
int total_num;
int dataLen;
char *send_buf;//char* 代表 字符指针类型，当其指向一个字符串的第一个元素时，它就可以代表这个字符串了
int listenfd,connfd,recvLen;

//16进制转10进制
int Dis_uNum(uint16 uValue)
{
	uint8 i;
	char cData[5]={'0','0','0','0','0'};
	cData[0] = uValue % 100000 / 10000 + '0';
  	cData[1] = uValue % 10000 / 1000 + '0';
  	cData[2] = uValue % 1000 / 100 + '0';
  	cData[3] = uValue % 100 / 10 + '0';
  	cData[4] = uValue % 10 / 1 + '0';
	if(0!=uValue)
	{
		for(i=0;i<5;i++)
		{
			if('0' != cData[i])
			break;
			if('0' == cData[i])
				cData[i]=' ';
		}
	}
	else if(0 == uValue)
	{
		for(i=0;i<4;i++)
		{
			cData[i]=' ';
		}
	}
	int value=atoi(cData)/100;//将所得10进制数转为整型数
	return value;
}

void onNewNode(void *arg, unsigned short nwkAddr, unsigned short parAddr, unsigned char macAddr[8])
{
	// 有新节点加入网络,将这个节点的短地址输出到终端
	printf("A New node coming, address is %04X\n", nwkAddr);
}
void onNewFunc (void *arg, unsigned short nwkAddr, int funcNum, WSNCOMM_NODE_FUNC *funcList)
{
	// 节点的功能被发现,将这些节点的功能输出到终端
	int i;
;
	printf("The node %04X has %d functions:\n", nwkAddr, funcNum);
	for(i = 0; i < funcNum; i++)
	{
		printf("Function %d: funcCode - %02X, funcID - %02X, refresh cycle - %d\n",
				i, funcList[i].funcCode, funcList[i].funcID, funcList[i].rCycle);
	}
}
void onNewData(void *arg, unsigned short nwkAddr, int endPoint, int funcCode, int funcID, char *data, int len)
{
	// 节点有新数据,将数据输出到终端
	int i,j;
	char infoArry[2];
	uint16 uInfo,uValue;
	int value;
	WSNCOMM_NODE *wsncom_node;
	WSNCOMM_NODE *node;

	wsncom_node=(WSNCOMM_NODE*)malloc(sizeof(WSNCOMM_NODE));
	node=(WSNCOMM_NODE*)malloc(sizeof(WSNCOMM_NODE)*10);

	total_num=wsncomm_getAllNode("127.0.0.1",&node);//获得所有节点的信息，存到node里

	printf("The node %04X emit a new data:", nwkAddr);
	for(i = 0; i < len; i++)
		printf("%02X ", data[i] & 0xFF);
	printf("\n");

	for(i=0;i<total_num;i++)
	{
		//循环判断当前的新数据节点的网络地址与所有节点的网络地址，若匹配成功，获得该节点的物理地址，并将前两位存入infoArry数组中
		if(node[i].nwkAddr==nwkAddr)
		{
			printf("info:");
			for(j=0;j<2;j++)
			{
				infoArry[j]=node[i].hwAddr[j];
				printf("%02X ",infoArry[j]);
			}
			
		}
	}
	uInfo=infoArry[0]<<8|infoArry[1];//将存有物理地址的数组转为无符号16进制数
	uValue=data[1]<<8|data[0];//将新数据交换位置转为无符号16进制数
	value=Dis_uNum(uValue);//将新数据转为10进制
	sprintf(send_buf,"/%04X:%d",uInfo,uValue);//以"/%04X:%d"格式将物理地址和新数据打印到send_buf
	printf("send_data: %s",send_buf);
	send(connfd,send_buf,9,0);从已经连接的套接字发送数据
	memset(send_buf,0,9);//将send_buf中填充0
}
void onNodeGone(void *arg, unsigned short nwkAddr)
{
	// 节点掉线,输出提示信息到终端
	printf("The node %04X gone!\n", nwkAddr);
}
void onServerGone(void *arg)
{
	// 服务程序断线,输出提示信息到终端
	printf("The server has gone!\n");
}
int main()
{
	int i,j;
	wsncom_node=(WSNCOMM_NODE*)malloc(sizeof(WSNCOMM_NODE));
	node=(WSNCOMM_NODE*)malloc(sizeof(WSNCOMM_NODE)*10);
	send_buf=(char*)malloc(sizeof(char*));
	
	total_num=wsncomm_getAllNode("127.0.0.1",&node);//获得当前网络中所有节点的信息，node用于存放返回的节点的信息
	printf("total:%d\n",total_num);
	for(i=0;i<total_num;i++)

	{
		printf("%04X  ,%04X  ,%d   ,%d  "  ,node[i].nwkAddr,node[i].parAddr,node[i].funcInfo[i].funcCode,node[i].funcInfo[i].funcID);
		for(j=0;j<2;j++)
		{
			printf("%02X",node[i].hwAddr[j]);//打印节点的物理地址
		}	
	printf("\n");
	}
	char *recvBuffer=(char *)malloc(4002);
	struct sockaddr_in serverAddr,clientAddr;//ipv4协议族套接字也叫网际套接口地址结构
	int clientAddrSize;
	printf("TCP Server Started...\n");
	listenfd=socket(AF_INET,SOCK_STREAM,0);//创建一个套接字，使用ipv4协议AF_INET，采用字节流套接口SOCK_STREAM；返回套接口描述字，0表示选择给定默认值
	if(listenfd == -1)//listenfd 负整数表示创建失败
	{
		printf("Invalid socket\n");
		exit(1);
	}
	bzero(&serverAddr,sizeof(serverAddr));将serverAddr结构体设置为0
	serverAddr.sin_family=AF_INET;
	serverAddr.sin_port=htons(port);以主机字节序表示的16位整数转换为网络字节序表示的整数
	serverAddr.sin_addr.s_addr=htonl(INADDR_ANY);//把主机字节序表示的32位整数转换为网络字节序表示的整数
	printf("Binding server socket to port %d\n",port);
	if(bind(listenfd,(struct sockaddr*)&serverAddr,sizeof(struct sockaddr)) == -1)//将指定协议地址绑定到套接字上并判断是否成功
	{
		printf("Bad bind\n");
		exit(1);	
	}
	if(listen(listenfd,1) == -1)//将套接口转换为被动状态，等待客户端连接请求，最大允许连接到请求数量为1
	{
		printf("Bad listen\n");
		exit(1);
	}
	printf("Accepting connections(Max 1 connections)...\n");
	for(;;)
	{
		clientAddrSize=sizeof(struct sockaddr_in);
		connfd=accept(listenfd,(struct sockaddr *)&clientAddr,&clientAddrSize);//从连接队列里获取已完成的连接，执行成功后返回一个非负的连接套接口描述字
		if(connfd == -1)
		{
			printf("Bad accept\n");
			exit(1);
		}
		while((recvLen=recv(connfd,recvBuffer,4000,0))>0)//从已经连接的套接口接收数据
		{
			void *user = wsncomm_register("127.0.0.1",		// 连接到本机的服务程序
							onNewNode,			// 注册发现新节点的回调函数
							onNewFunc,			// 注册发现新功能的回调函数
							onNewData,			// 注册新数据的回调函数
							onNodeGone,			// 注册节点掉线的回调函数
							onServerGone,		// 注册服务程序断线的回调函数
							NULL);				// 回调函数参数
			// 判断注册是否成功
			if(user == NULL)
			{
				printf("Register failed!\n");
				return 1;
			}
			while(1)
			{
				// 主循环,可以做任何其他的事情,Zigbee网络发生变化时会在相应的回调函数中执行

				sleep(5000);
			}
		}
		printf("Connection closed.\n");
		close(connfd);//关闭套接字
	}
	return 0;
}
