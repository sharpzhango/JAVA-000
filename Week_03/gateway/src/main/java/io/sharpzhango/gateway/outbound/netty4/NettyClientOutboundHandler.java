package io.sharpzhango.gateway.outbound.netty4;

import io.sharpzhango.gateway.outbound.OutboundHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class NettyClientOutboundHandler implements OutboundHandler {
    private NettyHttpClient client;

    public NettyClientOutboundHandler(String proxyServer){
        this.client = new NettyHttpClient(proxyServer);
    }

    @Override
    public void handle(FullHttpRequest fullRequest, ChannelHandlerContext ctx) throws InterruptedException {
        // @TODO 参考了`汤辉` `黄健` 同学 传递父PrentContext 的代码后，被他的思路理清了；重新写的，简单很多
        client.connect(fullRequest, ctx);


        // @TODO 之前方法
//        client.connect(fullRequest.uri());
//        handleResponse(fullRequest, ctx);
    }


    // @TODO 之前方法，还是按照httpclient的方式去处理，纠结点在 NettyInboundHandler#channelRead() 如何将读到的数据返回过来
    @Deprecated
    private void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {
        FullHttpResponse response = null;
        try {
            // @TODO 如何获取 ChannelInboundHandlerAdapter#channelRead 返回的数据？
            // @TODO  暂时用静态变量接受
            String value = NettyInboundHandler.result;


            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(value.getBytes()));
            response.headers().add(fullRequest.headers());
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", value.length());

        } catch (Exception e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    //response.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
            }
            ctx.flush();
            //ctx.close();
        }

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
