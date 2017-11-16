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
char *send_buf;//char* ���� �ַ�ָ�����ͣ�����ָ��һ���ַ����ĵ�һ��Ԫ��ʱ�����Ϳ��Դ�������ַ�����
int listenfd,connfd,recvLen;

//16����ת10����
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
	int value=atoi(cData)/100;//������10������תΪ������
	return value;
}

void onNewNode(void *arg, unsigned short nwkAddr, unsigned short parAddr, unsigned char macAddr[8])
{
	// ���½ڵ��������,������ڵ�Ķ̵�ַ������ն�
	printf("A New node coming, address is %04X\n", nwkAddr);
}
void onNewFunc (void *arg, unsigned short nwkAddr, int funcNum, WSNCOMM_NODE_FUNC *funcList)
{
	// �ڵ�Ĺ��ܱ�����,����Щ�ڵ�Ĺ���������ն�
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
	// �ڵ���������,������������ն�
	int i,j;
	char infoArry[2];
	uint16 uInfo,uValue;
	int value;
	WSNCOMM_NODE *wsncom_node;
	WSNCOMM_NODE *node;

	wsncom_node=(WSNCOMM_NODE*)malloc(sizeof(WSNCOMM_NODE));
	node=(WSNCOMM_NODE*)malloc(sizeof(WSNCOMM_NODE)*10);

	total_num=wsncomm_getAllNode("127.0.0.1",&node);//������нڵ����Ϣ���浽node��

	printf("The node %04X emit a new data:", nwkAddr);
	for(i = 0; i < len; i++)
		printf("%02X ", data[i] & 0xFF);
	printf("\n");

	for(i=0;i<total_num;i++)
	{
		//ѭ���жϵ�ǰ�������ݽڵ�������ַ�����нڵ�������ַ����ƥ��ɹ�����øýڵ�������ַ������ǰ��λ����infoArry������
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
	uInfo=infoArry[0]<<8|infoArry[1];//�����������ַ������תΪ�޷���16������
	uValue=data[1]<<8|data[0];//�������ݽ���λ��תΪ�޷���16������
	value=Dis_uNum(uValue);//��������תΪ10����
	sprintf(send_buf,"/%04X:%d",uInfo,uValue);//��"/%04X:%d"��ʽ�������ַ�������ݴ�ӡ��send_buf
	printf("send_data: %s",send_buf);
	send(connfd,send_buf,9,0);���Ѿ����ӵ��׽��ַ�������
	memset(send_buf,0,9);//��send_buf�����0
}
void onNodeGone(void *arg, unsigned short nwkAddr)
{
	// �ڵ����,�����ʾ��Ϣ���ն�
	printf("The node %04X gone!\n", nwkAddr);
}
void onServerGone(void *arg)
{
	// ����������,�����ʾ��Ϣ���ն�
	printf("The server has gone!\n");
}
int main()
{
	int i,j;
	wsncom_node=(WSNCOMM_NODE*)malloc(sizeof(WSNCOMM_NODE));
	node=(WSNCOMM_NODE*)malloc(sizeof(WSNCOMM_NODE)*10);
	send_buf=(char*)malloc(sizeof(char*));
	
	total_num=wsncomm_getAllNode("127.0.0.1",&node);//��õ�ǰ���������нڵ����Ϣ��node���ڴ�ŷ��صĽڵ����Ϣ
	printf("total:%d\n",total_num);
	for(i=0;i<total_num;i++)

	{
		printf("%04X  ,%04X  ,%d   ,%d  "  ,node[i].nwkAddr,node[i].parAddr,node[i].funcInfo[i].funcCode,node[i].funcInfo[i].funcID);
		for(j=0;j<2;j++)
		{
			printf("%02X",node[i].hwAddr[j]);//��ӡ�ڵ�������ַ
		}	
	printf("\n");
	}
	char *recvBuffer=(char *)malloc(4002);
	struct sockaddr_in serverAddr,clientAddr;//ipv4Э�����׽���Ҳ�������׽ӿڵ�ַ�ṹ
	int clientAddrSize;
	printf("TCP Server Started...\n");
	listenfd=socket(AF_INET,SOCK_STREAM,0);//����һ���׽��֣�ʹ��ipv4Э��AF_INET�������ֽ����׽ӿ�SOCK_STREAM�������׽ӿ������֣�0��ʾѡ�����Ĭ��ֵ
	if(listenfd == -1)//listenfd ��������ʾ����ʧ��
	{
		printf("Invalid socket\n");
		exit(1);
	}
	bzero(&serverAddr,sizeof(serverAddr));��serverAddr�ṹ������Ϊ0
	serverAddr.sin_family=AF_INET;
	serverAddr.sin_port=htons(port);�������ֽ����ʾ��16λ����ת��Ϊ�����ֽ����ʾ������
	serverAddr.sin_addr.s_addr=htonl(INADDR_ANY);//�������ֽ����ʾ��32λ����ת��Ϊ�����ֽ����ʾ������
	printf("Binding server socket to port %d\n",port);
	if(bind(listenfd,(struct sockaddr*)&serverAddr,sizeof(struct sockaddr)) == -1)//��ָ��Э���ַ�󶨵��׽����ϲ��ж��Ƿ�ɹ�
	{
		printf("Bad bind\n");
		exit(1);	
	}
	if(listen(listenfd,1) == -1)//���׽ӿ�ת��Ϊ����״̬���ȴ��ͻ���������������������ӵ���������Ϊ1
	{
		printf("Bad listen\n");
		exit(1);
	}
	printf("Accepting connections(Max 1 connections)...\n");
	for(;;)
	{
		clientAddrSize=sizeof(struct sockaddr_in);
		connfd=accept(listenfd,(struct sockaddr *)&clientAddr,&clientAddrSize);//�����Ӷ������ȡ����ɵ����ӣ�ִ�гɹ��󷵻�һ���Ǹ��������׽ӿ�������
		if(connfd == -1)
		{
			printf("Bad accept\n");
			exit(1);
		}
		while((recvLen=recv(connfd,recvBuffer,4000,0))>0)//���Ѿ����ӵ��׽ӿڽ�������
		{
			void *user = wsncomm_register("127.0.0.1",		// ���ӵ������ķ������
							onNewNode,			// ע�ᷢ���½ڵ�Ļص�����
							onNewFunc,			// ע�ᷢ���¹��ܵĻص�����
							onNewData,			// ע�������ݵĻص�����
							onNodeGone,			// ע��ڵ���ߵĻص�����
							onServerGone,		// ע����������ߵĻص�����
							NULL);				// �ص���������
			// �ж�ע���Ƿ�ɹ�
			if(user == NULL)
			{
				printf("Register failed!\n");
				return 1;
			}
			while(1)
			{
				// ��ѭ��,�������κ�����������,Zigbee���緢���仯ʱ������Ӧ�Ļص�������ִ��

				sleep(5000);
			}
		}
		printf("Connection closed.\n");
		close(connfd);//�ر��׽���
	}
	return 0;
}
