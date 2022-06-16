#include <unistd.h>
#include <iostream>
#include <string.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

int main()
{
    /*
    sudo lsof -PiTCP -sTCP:LISTEN.
    sudo lsof -i :{찾은포트}.
    sudo kill -9 {찾은PID}.
    */
    const std::string IP = "127.0.0.1";
    const unsigned short PORT = 8080;
    sockaddr_in addrListen, addrClient;
    int fdListen, fdClient;
    char temp[20];
    socklen_t lenAddrClient;

    bzero(static_cast<void *>(&addrListen), sizeof(addrListen));
    // sock addr 설정
    addrListen.sin_family = AF_INET;
    addrListen.sin_port = htons(PORT);
        //https://man7.org/linux/man-pages/man3/inet_pton.3.html
    inet_pton(AF_INET, IP.data(), static_cast<void *>(&addrListen.sin_addr));
    // listen 소켓 생성
    // https://www.bangseongbeom.com/af-inet-vs-pf-inet.html -> PF_INET vs AF_INET
    fdListen = socket(PF_INET, SOCK_STREAM, 0);
    if (fdListen == -1)
    {
        std::cerr << "socket error occur" << std::endl;
    }
    // bind
    if (bind(fdListen, reinterpret_cast<const sockaddr*>(&addrListen), sizeof(addrListen)) == -1)
    {
        std::cerr << "bind error occur" << std::endl;
    }
    // listen
    // backlog
    if (listen(fdListen, 5) == -1)
    {
        std::cerr << "listen error occur" << std::endl;
    }
    // while (1)
    // {
        lenAddrClient = sizeof(addrClient);
        fdClient = accept(fdListen, reinterpret_cast<sockaddr*>(&addrClient), &lenAddrClient);
        if (fdClient < 0)
        {
            std::cerr << "accept fail" << std::endl;
        }
        else
        {
            inet_ntop(AF_INET, &addrClient.sin_addr.s_addr, temp, sizeof(temp));
            printf("%s client connected.\n", temp);
        }
        close(fdClient);
    // }
    close(fdListen);
}