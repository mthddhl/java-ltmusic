package com.cumt.musicpay;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class Test {
    public static void main(String[] args) {
        AutoGenerator autoGenerator=new AutoGenerator();

        DataSourceConfig database=new DataSourceConfig();
        database.setDriverName("com.mysql.cj.jdbc.Driver");
        database.setUrl("jdbc:mysql://localhost:3306/musicpay?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC");
        database.setUsername("root");
        database.setPassword("147236");
        autoGenerator.setDataSource(database);

        GlobalConfig globalConfig=new GlobalConfig();
        globalConfig.setOutputDir("G:\\java\\projectMusic\\musicAll\\musicPay\\src\\main\\java");
        globalConfig.setOpen(false);
        globalConfig.setAuthor("mthddhl");
        globalConfig.setFileOverride(true);
        globalConfig.setMapperName("%sDao");
        globalConfig.setIdType(IdType.ASSIGN_ID);
        autoGenerator.setGlobalConfig(globalConfig);

        PackageConfig packageConfig=new PackageConfig();
        packageConfig.setParent("com.cumt.musicpay");
        packageConfig.setEntity("domain");
        packageConfig.setMapper("dao");
        autoGenerator.setPackageInfo(packageConfig);

        StrategyConfig setPackageInfo=new StrategyConfig();
//        setPackageInfo.setInclude()
        setPackageInfo.setTablePrefix("t_");
        setPackageInfo.setRestControllerStyle(true);
        setPackageInfo.setNaming(NamingStrategy.underline_to_camel);
        setPackageInfo.setColumnNaming(NamingStrategy.underline_to_camel);
        setPackageInfo.setVersionFieldName("version");
        setPackageInfo.setLogicDeleteFieldName("deleted");
        setPackageInfo.setEntityLombokModel(true);
        autoGenerator.setStrategy(setPackageInfo);


        autoGenerator.execute();
    }
}
