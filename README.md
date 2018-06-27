流程：
---
client --》 clientHandler channelActive(客户端写入数据到socket channel中) ——》server ——》 serverHandler channelRead
(服务端从socketchannel中读取数据 处理完成后可以继续把数据写入socketchannel 作为服务端给客户端的应答数据) ——》clientHandler
channelRead (收到服务端的应答后)——》