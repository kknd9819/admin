package com.zz.config.context;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.zz.config.template.directive.FlashMessageDirective;
import com.zz.config.template.directive.PaginationDirective;
import com.zz.config.template.method.CurrencyMethod;
import com.zz.config.template.method.MessageMethod;

/**
 * Created by X-man on 2017/5/8.
 */
@Configuration
public class ApplicationContext{
	
	@Resource(name="currencyMethod")
	protected CurrencyMethod currencyMethod;
	
	@Resource(name="messageMethod")
	protected MessageMethod messageMethod;
	
	@Resource(name="flashMessageDirective")
	protected FlashMessageDirective flashMessageDirective;

	@Resource(name="paginationDirective")
	protected PaginationDirective paginationDirective;
	
	
    @Bean
    public DefaultKaptcha defaultKaptcha(){
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border","yes");
        properties.setProperty("kaptcha.border.color","221,221,221");
        properties.setProperty("kaptcha.textproducer.font.color","0,0,0");
        properties.setProperty("kaptcha.image.width","100");
        properties.setProperty("kaptcha.textproducer.font.size","30");
        properties.setProperty("kaptcha.image.height","38");
        properties.setProperty("kaptcha.session.key","captchaCode");
        properties.setProperty("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise");
        properties.setProperty("kaptcha.textproducer.char.length","4");
        properties.setProperty("kaptcha.textproducer.font.names","microsoft yahei,Arial");
        properties.setProperty("kaptcha.background.clear.from","246,246,246");
        properties.setProperty("kaptcha.word.impl","com.google.code.kaptcha.text.impl.MyWordRenderer");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    @Bean(initMethod = "init",destroyMethod = "close")
    public DruidDataSource druidDataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/admin?useUnicode=true&characterEncoding=utf-8");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("Zheng1234");
        druidDataSource.setInitialSize(10);
        druidDataSource.setMinIdle(10);
        druidDataSource.setMaxActive(100);
        druidDataSource.setMaxWait(60000);
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(300000);
        druidDataSource.setValidationQuery("select 1");
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        return druidDataSource;
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() throws SQLException {
        return new NamedParameterJdbcTemplate(druidDataSource());
    }

    @Bean
    public SimpleJdbcCall simpleJdbcCall() throws SQLException {
        return new SimpleJdbcCall(druidDataSource());
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setMaxPoolSize(50);
        threadPoolTaskExecutor.setQueueCapacity(1000);
        threadPoolTaskExecutor.setKeepAliveSeconds(60);
        return threadPoolTaskExecutor;
    }
    
    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver(){
    	SimpleMappingExceptionResolver s = new SimpleMappingExceptionResolver();
    	s.setWarnLogCategory("WARN");
    	s.setDefaultErrorView("/common/error");
    	s.setDefaultStatusCode(500);
    	return s;
    }

    
    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer(){
    	FreeMarkerConfigurer f = new FreeMarkerConfigurer();
    	f.setTemplateLoaderPaths("classpath:/templates/");
    	Properties prop = new Properties();
    	prop.setProperty("defaultEncoding", "UTF-8");
    	prop.setProperty("locale", "zh_CN");
    	prop.setProperty("template_update_delay", "0");
    	prop.setProperty("tag_syntax", "auto_detect");
    	prop.setProperty("whitespace_stripping", "true");
    	prop.setProperty("classic_compatible", "true");
    	prop.setProperty("number_format", "0");
    	prop.setProperty("boolean_format", "true,false");
    	prop.setProperty("datetime_format", "yyyy-MM-dd");
    	prop.setProperty("date_format", "yyyy-MM-dd");
    	prop.setProperty("time_format", "HH:mm:ss");
    	prop.setProperty("object_wrapper", "freemarker.ext.beans.BeansWrapper");
    	f.setFreemarkerSettings(prop);
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("systemName", "云生源");
    	map.put("systemVersion", "1.0 RELEASE");
    	map.put("systemDescription", "云生源是生源商业集团重磅推出的金融商业平台");
    	map.put("systemShowPowered", "true");
    	map.put("interval", "60000");
    	map.put("locale", "zh_CN");
    	map.put("siteUrl", "http://www.3liao.top");
    	map.put("abbreviate", "abbreviateMethod");
    	map.put("currency",currencyMethod);
    	map.put("message",messageMethod);
    	map.put("flash_message", flashMessageDirective);
    	map.put("pagination", paginationDirective);
    	f.setFreemarkerVariables(map);
    	return f;
    }
    
    @Bean
    public FixedLocaleResolver fixedLocaleResolver(){
    	FixedLocaleResolver f = new FixedLocaleResolver();
    	Locale locale = new Locale("zh_CN");
    	f.setDefaultLocale(locale);
    	return f;
    }
    
//    @Bean
//    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() throws IOException{
//    	EhCacheManagerFactoryBean e = new EhCacheManagerFactoryBean();
//    	e.setConfigLocation(new ClassPathResource ("config/ehcache-shiro.xml"));
//    	e.setShared(true);
//    	return e;
//    }
//    
//    @Bean
//    public EhCacheCacheManager ehCacheCacheManager(EhCacheManagerFactoryBean bean) throws IOException{
//    	EhCacheCacheManager e = new EhCacheCacheManager();
//    	e.setCacheManager(bean.getObject());
//    	return e;
//    }
    

}
