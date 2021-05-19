package com.atguigu.snowflake.util;


import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.Ipv4Util;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

@Slf4j
public class IdGeneratorSnowflake {
    private Long workerId = 0L;
    private Long datacenterId = 1L;
    private Snowflake snowflake = IdUtil.createSnowflake(workerId, datacenterId);

    @PostConstruct //@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次。
    public void init() {
        try {
            workerId = Ipv4Util.ipv4ToLong(NetUtil.getLocalhostStr());
            log.info("当前机器的workerId：{}", workerId);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("当前机器的workerId获取失败", e);
            workerId = (long) NetUtil.getLocalhostStr().hashCode();
        }
    }

    public synchronized long snowflakeId() {
        return snowflake.nextId();
    }

    public synchronized long snowflakeId(Long workerId, Long datacenterId) {
        snowflake = IdUtil.createSnowflake(workerId, datacenterId);
        return snowflake.nextId();
    }

    public static void main(String[] args) {
        System.out.println(new IdGeneratorSnowflake().snowflake.nextId());
    }
}
