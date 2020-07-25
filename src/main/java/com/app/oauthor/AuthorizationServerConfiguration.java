package com.app.oauthor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(ClientDetailsServiceConfigurer configurer)throws Exception{
        //自动从数据库查数据
        //configurer.withClientDetails(detailsService());

        configurer.inMemory()
                .withClient("browser")
                .secret(passwordEncoder.encode("secret"))
                .authorizedGrantTypes("password","authorization_code","refresh_token")
                .scopes("all");
                //.redirectUris("http://www.callback.com");//注册回调地址
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer configurer)throws Exception{
        configurer.tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);

//        DefaultTokenServices tokenServices =new DefaultTokenServices();
//        tokenServices.setTokenStore(configurer.getTokenStore());
//        tokenServices.setSupportRefreshToken(true);
//        tokenServices.setClientDetailsService(configurer.getClientDetailsService());
//        tokenServices.setTokenEnhancer(configurer.getTokenEnhancer());
//        //1小时
//        tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.HOURS.toSeconds(2));
//        //1天
//        tokenServices.setRefreshTokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(1));
//        tokenServices.setReuseRefreshToken(false);
//        configurer.tokenServices(tokenServices);
    }

    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

//    @Bean
//    public ClientDetailsService detailsService(){
//        return new JdbcClientDetailsService(dataSource);
//    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security)throws Exception{
        security.allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }
}

