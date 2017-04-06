package com.example;

import com.hazelcast.config.Config;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.UserCodeDeploymentConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * Created by cq on 11/2/17.
 */
@Configuration
public class ConfigHazelcast {

    @Bean
    HazelcastInstance hazelcastInstance() {

        Config config = new Config();
        config.setLiteMember(true);
        config.setLicenseKey("YOUR_LICENSE_KEY");
        UserCodeDeploymentConfig distCLConfig = config.getUserCodeDeploymentConfig();
        distCLConfig.setEnabled(true)
        .setClassCacheMode(UserCodeDeploymentConfig.ClassCacheMode.ETERNAL)
                .setProviderMode(UserCodeDeploymentConfig.ProviderMode.LOCAL_CLASSES_ONLY)
                .setWhitelistedPrefixes("com.example.model");

        NetworkConfig networkConfig = config.getNetworkConfig();

        networkConfig.getInterfaces().setEnabled(true).addInterface("10.*.*.*");

        JoinConfig join = networkConfig.getJoin();
        join.getMulticastConfig().setEnabled(false);
        join.getTcpIpConfig().setEnabled(true);

        String servicesJson = System.getenv("VCAP_SERVICES");
        if (servicesJson == null || servicesJson.isEmpty()) {
            System.err.println("No service found!!!");
            return null;
        }

        BasicJsonParser parser = new BasicJsonParser();
        Map<String, Object> json = parser.parseMap(servicesJson);
        List hazelcast = (List) json.get("hazelcast");
        Map map = (Map) hazelcast.get(0);
        Map credentials = (Map) map.get("credentials");
        String groupName = (String) credentials.get("group_name");
        String groupPassword = (String) credentials.get("group_pass");
        List<String> members = (List<String>) credentials.get("members");

        GroupConfig groupConfig = config.getGroupConfig();
        groupConfig.setName(groupName).setPassword(groupPassword);


        for (String member : members) {
            join.getTcpIpConfig().addMember(member.replace('"', ' ').trim() + ":5701");
        }

        return Hazelcast.newHazelcastInstance(config);
    }
}
