package io.kimmking.rpcfx.api;

public interface RpcfxResolver {

    Object resolve(String serviceClass);// 查找具体的业务服务实现

}