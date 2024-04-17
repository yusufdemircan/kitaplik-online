package kitaplik.bookservice.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NetworkConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfiguration {
    @Bean
    public Config hazelCastConfig() {
        Config config = new Config();
        config.getManagementCenterConfig().setConsoleEnabled(true);

        config.setInstanceName("hazelcast-instance")
                .addMapConfig(
                        new MapConfig()
                                .setName("hazelcastMap")
                                .setTimeToLiveSeconds(-1));
        NetworkConfig network = config.getNetworkConfig();
        network.setPortCount(3);
        JoinConfig join = network.getJoin();
        join.getMulticastConfig().setEnabled(false);
        join.getTcpIpConfig().addMember("127.0.0.1").addMember("127.0.0.1").addMember("127.0.0.1").setEnabled(true);
        network.getInterfaces().setEnabled(true).addInterface("127.0.0.1").addInterface("127.0.0.1").addInterface("127.0.0.1");
        return config;
    }
}