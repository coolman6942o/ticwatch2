package com.mobvoi.ticwatch.base.generator.account;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @Project : ticwatch_backend
 * @Package Name : com.mobvoi.ticwatch.module.base.generator
 * @Author : xiekun
 * @Desc :
 * @Create Date : 2021年07月22日 15:12
 * ----------------- ----------------- -----------------
 */
public class CodeGenerator {


  /**
   * 模块名字
   **/
  public final static String moduleName = "generator";

  /**
   * 生成的Java文件的路径
   **/
  public final static String packagepath = moduleName + "/src/main/java";

  /**
   * 生成的mybatis的xml的路径
   **/
  public final static String mapperXmlPath = moduleName + "/src/main/resources/mapper";

  public final static String projectPath = System.getProperty("user.dir");

  public final static String packageName = "com.mobvoi.ticwatch";

  /**
   * 数据库配置
   */
  public final static String db_url = "jdbc:mysql://127.0.0.1:3306/ticaccount?useUnicode=true&useSSL=false&characterEncoding=utf8";
  public final static String db_driverName = "com.mysql.cj.jdbc.Driver";
  public final static String db_username = "root";
  public final static String db_password = "123456";


  /**
   * 设置包名
   */
  public static PackageConfig getPackageConfig() {
    PackageConfig pc = new PackageConfig();
    //pc.setModuleName(scanner("模块名"));
    pc.setParent(packageName);
    return pc;
  }

  /**
   * 设置全局配置
   */
  public static GlobalConfig getGlobalConfig() {
    GlobalConfig gc = new GlobalConfig();
    gc.setOutputDir(projectPath + "/" + packagepath);
    gc.setAuthor("xiekun");
    gc.setOpen(false);
    // 是否覆盖同名文件，默认是false
    gc.setFileOverride(true);
    //不需要ActiveRecord特性的请改为false
    gc.setActiveRecord(true);
//      // XML 二级缓存
//      gc.setEnableCache(false);
    // XML ResultMap
    gc.setBaseResultMap(true);
    // XML columList
    gc.setBaseColumnList(true);
    //实体属性 Swagger2 注解
    gc.setSwagger2(true);
    // 自定义文件命名，注意 %s 会自动填充表实体属性！
    gc.setEntityName("%sEntity");
    gc.setMapperName("%sMapper");
    gc.setXmlName("%sMapper");
    gc.setServiceName("%sService");
    gc.setServiceImplName("%sServiceImpl");
    gc.setControllerName("%sController");
    return gc;
  }

  /**
   * 数据源配置
   */
  public static DataSourceConfig getDataSourceConfig() {
    DataSourceConfig dsc = new DataSourceConfig();
    dsc.setUrl(db_url);
    // dsc.setSchemaName("public");
    dsc.setDriverName(db_driverName);
    dsc.setUsername(db_username);
    dsc.setPassword(db_password);
    return dsc;
  }


  /**
   * 自定义xml
   */
  public static InjectionConfig XmlInjectionConfig() {
    InjectionConfig cfg = new InjectionConfig() {
      @Override
      public void initMap() {
        // to do nothing
      }
    };

    // 如果模板引擎是 freemarker
    String templatePath = "/templates/mapper.xml.ftl";
    // 如果模板引擎是 velocity
    // String templatePath = "/templates/mapper.xml.vm";
    // 自定义输出配置
    List<FileOutConfig> focList = new ArrayList<>();
    // 自定义配置会被优先输出
    focList.add(new FileOutConfig(templatePath) {
      @Override
      public String outputFile(TableInfo tableInfo) {
        // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
        return projectPath + "/" + mapperXmlPath + "/" + tableInfo.getEntityName() + "Mapper"
            + StringPool.DOT_XML;
      }
    });
        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录");
                return false;
            }
        });
        */
    cfg.setFileOutConfigList(focList);
    return cfg;
  }

  /**
   * 策略配置
   */
  private static StrategyConfig getStrategyConfig() {
    StrategyConfig strategy = new StrategyConfig();
    strategy.setNaming(NamingStrategy.underline_to_camel);
    //数据库字段生成java字段时，取消驼峰的映射关系，在字段上指定java字段对应的表字段名
    strategy.setColumnNaming(NamingStrategy.no_change);
//      strategy.setColumnNaming(NamingStrategy.underline_to_camel);
    strategy.setEntityLombokModel(true);
    strategy.setRestControllerStyle(true);
//        // 公共父类
//        strategy.setSuperControllerClass("com.mdl.hundun.common.BaseController");
//      // 写于父类中的公共字段
//      strategy.setSuperEntityColumns("id", "create_time", "update_time", "is_deleted");
//      strategy.setSuperEntityClass("com.mobvoi.zhaopin.console.fn.common.BaseEntity");

    strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
    //驼峰转连字符
    strategy.setControllerMappingHyphenStyle(true);
    strategy.setTablePrefix(getPackageConfig().getModuleName() + "_");
    return strategy;
  }

  /**
   * 自定义模板
   */
  private static TemplateConfig getTemplateConfig() {
    TemplateConfig templateConfig = new TemplateConfig();
    // 配置自定义输出模板
    //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
    //templateConfig.setEntity("templates/entity2.java");
    // templateConfig.setService();
    // templateConfig.setController();

    templateConfig.setXml(null);
    return templateConfig;
  }

  public static void main(String[] args) {
    // 全局配置
    GlobalConfig gc = getGlobalConfig();
    // 数据源配置
    DataSourceConfig dsc = getDataSourceConfig();
    // 包配置
    PackageConfig pc = getPackageConfig();
    // 自定义配置
    InjectionConfig cfg = XmlInjectionConfig();
    // 配置模板
    TemplateConfig templateConfig = getTemplateConfig();
    // 策略配置
    StrategyConfig strategy = getStrategyConfig();

    // 代码生成器
    new AutoGenerator()
        .setGlobalConfig(gc)
        .setDataSource(dsc)
        .setPackageInfo(pc)
        .setCfg(cfg)
        .setTemplate(templateConfig)
        .setStrategy(strategy)
        .setTemplateEngine(new FreemarkerTemplateEngine())
        .execute();
  }


  /**
   * <p>
   * 读取控制台内容
   * </p>
   */
  public static String scanner(String tip) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("请输入" + tip + "：");
    if (scanner.hasNext()) {
      String ipt = scanner.next();
      if (StringUtils.isNotEmpty(ipt)) {
        return ipt;
      }
    }
    throw new MybatisPlusException("请输入正确的" + tip + "！");
  }



}
