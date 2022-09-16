package cn.wuwenyao.db.doc.generator.service.impl;

import cn.wuwenyao.db.doc.generator.config.GeneratorConfig;
import cn.wuwenyao.db.doc.generator.dao.DbInfoDao;
import cn.wuwenyao.db.doc.generator.service.GeneratorService;

import java.io.IOException;

/***
 * 文档生成服务-抽象基类
 *
 * @author wwy shiqiyue.github.com
 *
 */
public abstract class AbstractGeneratorServiceImpl implements GeneratorService {

    protected DbInfoDao dbInfoDao;

    protected GeneratorConfig generatorConfig;

    @Override
    public void generateDbDoc() throws Exception {

    }

    @Override
    public void generate() throws Exception {
        generateDbDoc();
        openDir();
    }

    /***
     * 打开目录
     */
    private void openDir() throws IOException {
        // 弹出目标文件夹
        Runtime.getRuntime().exec("explorer " + generatorConfig.getTargetFileDir());
    }

    @Override
    public void setDbInfoDao(DbInfoDao dbInfoDao) {
        this.dbInfoDao = dbInfoDao;
    }

    @Override
    public void setGeneratorConfig(GeneratorConfig generatorConfig) {
        this.generatorConfig = generatorConfig;
    }


}
