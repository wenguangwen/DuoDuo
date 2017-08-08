package project.init.xmlparse;

import org.qiunet.template.creator.BaseXmlParse;
import project.init.elements.mybatisConfig.ElementMybatisConfig;
import project.init.elements.mybatisConfig.ExtraELementConfig;

/**
 * @author qiunet
 *         Created on 17/2/17 16:43.
 */
public class MybatisConfigXmlParse extends BaseXmlParse {

	public MybatisConfigXmlParse(String basePath, String xmlConfigPath) {
		super(ConfigVmElement.class, basePath, xmlConfigPath);
	}

	@Override
	public void parseXml() {
		this.addObjectCreate("base/config", ElementMybatisConfig.class);
		this.addObjectCreate("base/extraConfigs/extra", ExtraELementConfig.class, "addExtraConfig");
	}
}