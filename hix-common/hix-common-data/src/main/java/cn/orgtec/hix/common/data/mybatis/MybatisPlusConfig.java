package cn.orgtec.hix.common.data.mybatis;

import cn.orgtec.hix.common.data.datascope.DataScopeInterceptor;
import cn.orgtec.hix.common.data.tenant.HixTenantHandler;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lengleng
 * @date 2017/10/29
 */
@Configuration
@MapperScan("cn.orgtec.hix.**.mapper")
public class MybatisPlusConfig {

	/**
	 * 创建租户维护处理器对象
	 *
	 * @return 处理后的租户维护处理器
	 */
	@Bean
	@ConditionalOnMissingBean
	public HixTenantHandler pigxTenantHandler() {
		return new HixTenantHandler();
	}

	/**
	 * 分页插件
	 *
	 * @param tenantHandler 租户处理器
	 * @return PaginationInterceptor
	 */
	@Bean
	@ConditionalOnMissingBean
	public PaginationInterceptor paginationInterceptor(HixTenantHandler tenantHandler) {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		List<ISqlParser> sqlParserList = new ArrayList<>();
		TenantSqlParser tenantSqlParser = new TenantSqlParser();
		tenantSqlParser.setTenantHandler(tenantHandler);
		sqlParserList.add(tenantSqlParser);
		paginationInterceptor.setSqlParserList(sqlParserList);
		return paginationInterceptor;
	}

	/**
	 * 数据权限插件
	 *
	 * @param dataSource 数据源
	 * @return DataScopeInterceptor
	 */
	@Bean
	@ConditionalOnMissingBean
	public DataScopeInterceptor dataScopeInterceptor(DataSource dataSource) {
		return new DataScopeInterceptor(dataSource);
	}

	/**
	 * 逻辑删除插件
	 *
	 * @return LogicSqlInjector
	 */
	@Bean
	@ConditionalOnMissingBean
	public ISqlInjector sqlInjector() {
		return new LogicSqlInjector();
	}
}
