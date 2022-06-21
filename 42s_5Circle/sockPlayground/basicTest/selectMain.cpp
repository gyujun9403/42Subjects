#include <iostream>
#include <unistd.h>
#include <sys/socket.h>
#include <fcntl.h>      //fcntl
#include <netinet/in.h> //sockaddr_in
#include <arpa/inet.h>  //inet_pton
#include <vector>
#include <errno.h>

#define BUFFERSIZE 1024

// select : select, fd_set,

class Session
{
    private:
        int fdSock_;
        char recvBuffer_[BUFFERSIZE];
        int lenRecv_;
        int lenSend_;
    public:
        Session(int fdSock): fdSock_(fdSock), lenRecv_(-1), lenSend_(-1) {bzero(recvBuffer_, BUFFERSIZE);};
        void setFdSock(int fdSock) {this->fdSock_ = fdSock;};
        void setLenRecv(int lenRecv) {this->lenRecv_ = lenRecv;};
        void setLenSend(int lenSend) {this->lenSend_ = lenSend;};
        int getFdSock() const {return this->fdSock_;};
        char* getbufferPtr() {return static_cast<char *>(this->recvBuffer_);};
        int getLenRecv() const {return this->lenRecv_;};
        int getLenSend() const {return this->lenSend_;};
};

int main()
{
    fd_set rSet, wSet;
    sockaddr_in addrListen, addrClient;
    const char SERVER_IP[] = "127.0.0.1";
    const unsigned short SERVER_PORT = 8080;
    int fdListen, fdClient, selected;
    socklen_t lenAddrclient;

    /* 🌟 소켓 세팅 🌟 */
    fdListen = socket(PF_INET, SOCK_STREAM, 0);
    if (fdListen < 0)
    {
        std::cerr << "sock fail" << std::endl;
        return 1;
    }
    if (fcntl(fdListen, F_SETFL, O_NONBLOCK) < 0)
    {
        std::cerr << "fcntl fail" << std::endl;
        return 1;
    }
    addrListen.sin_family = AF_INET;
    addrListen.sin_port = htons(SERVER_PORT);
    //addrListen.sin_addr.s_addr
    inet_pton(AF_INET, SERVER_IP, &addrListen.sin_addr);
    /* 🌟 바인딩 및 리슨 🌟 */
    if (bind(fdListen, reinterpret_cast<sockaddr*>(&addrListen), sizeof(addrListen)) < 0)
    {
        std::cerr << "bind fail : "  << errno << std::endl;
        return 1;
    }

    //listen 이후 select하게 set에 등록
    if (listen(fdListen, SOMAXCONN) < 0)
    {
        std::cerr << "listen fail" << std::endl;
        return 1;
    }
    /* 🌟 세션 생성 🌟 */
    std::vector<Session> sessions;
    sessions.reserve(100);
    /* 🌟 서버 동작 🌟 */
    while (1)
    {
        /* 🌟 fd_set설정 🌟 */
        FD_ZERO(&rSet); FD_ZERO(&wSet);
        FD_SET(fdListen, &rSet);
        for (int i = 0; i < sessions.size(); i++)
        {
            FD_SET(sessions[i].getFdSock(), &rSet);
            std::cout << sessions[i].getFdSock() << " setted" << std::endl;
        }
        selected = select(1000, &rSet, NULL, NULL, NULL);
        /* 🌟 ⬇️TCP는 스트림이다!!!! 🌟 */
        //sleep(3);
        if (selected < 0)
        {
            std::cerr << "select fail :" << selected << ", " << errno << std::endl;
            return 1;
        }
        /* 🌟 새로운 클라 접속 시도 🌟 */
        if (FD_ISSET(fdListen, &rSet))
        {
            std::cout << "fdListen setted" << std::endl;
            lenAddrclient = sizeof(addrClient);
            fdClient = accept(fdListen, reinterpret_cast<sockaddr*>(&addrClient), &lenAddrclient);
            std::cout << "fdListen accepted" << std::endl;
            if (fdClient < 0)
            {
                std::cerr << "accept fail" << std::endl;
                return 1;
            }
            std::cout << "accept :" << fdClient << std::endl;
            sessions.push_back(Session(fdClient));
        }
        for (int i = 0; i < sessions.size(); i++)
        {
            if (FD_ISSET(sessions[i].getFdSock(), &rSet))
            {
                sessions[i].setLenRecv(recv(sessions[i].getFdSock(), sessions[i].getbufferPtr(), BUFFERSIZE, 0));
                if (sessions[i].getLenRecv() < 0)
                {
                    std::cerr << "recv fail" << std::endl;
                }
                else if (sessions[i].getLenRecv() == 0)
                {
                    std::cout << "disconnect :" << sessions[i].getFdSock() << std::endl;
                    close(sessions[i].getFdSock());
                    sessions.erase(sessions.begin() + i);
                }
                else
                {
                    std::string temp = std::to_string(sessions[i].getFdSock()) + " :";
                    temp.append(sessions[i].getbufferPtr(), sessions[i].getLenRecv());
                    //write(1, sessions[i].getbufferPtr(), sessions[i].getLenRecv());
                    std::cout << temp << std::endl;
                    sessions[i].setLenRecv(0);
                }
            }
        }
    }
    close(fdListen);
}