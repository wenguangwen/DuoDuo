package org.qiunet.project.init;

import java.io.File;
import java.io.IOException;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/5/18 16:54
 **/
public class DefaultProjectInitConfig implements IProjectInitConfig {
	@Override
	public String getBasePath() {
		String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "../../";
		File file  = new File(basePath);
		try {
			return file.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String getEntityXmlPath() {
		return "xml/entity_create.xml";
	}

	@Override
	public String getEntityInfoXmlPath() {
		return "xml/entity_info_create.xml";
	}

	@Override
	public String getMybatisConfigXmlPath() {
		return "xml/mybatis_config_create.xml";
	}

	@Override
	public String getMabatisMappingXmlPath() {
		return "xml/mybatis_mapping_create.xml";
	}
}
