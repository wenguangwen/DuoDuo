package project.init;

import org.qiunet.template.creator.BaseXmlParse;
import org.qiunet.template.creator.TemplateCreator;
import org.qiunet.template.parse.template.VelocityFactory;
import org.qiunet.template.parse.xml.SubVmElement;
import org.qiunet.template.parse.xml.VmElement;
import project.init.elements.entity.Entity;
import project.init.elements.info.EntityInfo;
import project.init.elements.info.EntityVo;
import project.init.elements.mapping.ElementMapping;
import project.init.xmlparse.EntityInfoXmlParse;
import project.init.xmlparse.EntityXmlParse;
import project.init.xmlparse.MybatisConfigXmlParse;
import project.init.xmlparse.MybatisMappingXmlParse;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiunet on 4/8/17.
 */
public final class ProjectInitCreator {
	private ProjectInitCreator(){}
	/***
	 * 根据模板创建玩家的对象
	 */
	public static void create(String basePath) {
		// 如果后期出现类错误, 没法编译通过, 无法生成的情况, 可以把这些类复制出去, 把basePath指向项目的目录, 也行.
		BaseXmlParse entityParse = new EntityXmlParse(basePath, "src/test/resources/xml/entity_create.xml");
		BaseXmlParse entityInfoParse = new EntityInfoXmlParse(basePath,"src/test/resources/xml/entity_info_create.xml");
		BaseXmlParse mybatisConfigParse = new MybatisConfigXmlParse(basePath,"src/test/resources/xml/mybatis_config_create.xml");
		BaseXmlParse mybatisMappingParse = new MybatisMappingXmlParse(basePath,"src/test/resources/xml/mybatis_mapping_create.xml");

		Map<String, VmElement<? extends SubVmElement>> params = new HashMap<>();
		try {
			VmElement<Entity> entityVmElements = new TemplateCreator(entityParse, params).parseTemplate();
			params.put("entity", entityVmElements);

			VmElement<EntityInfo> entityInfoVmElements = new TemplateCreator(entityInfoParse, params).parseTemplate();
			params.put("info", entityInfoVmElements);

			handlerVo(basePath, entityVmElements, entityInfoVmElements);

			VmElement<ElementMapping> mybatisMappingVmElement = new TemplateCreator(mybatisMappingParse, params).parseTemplate();
			params.put("mapping", mybatisMappingVmElement);

			VmElement<EntityInfo> mybatisConfigVmElements = new TemplateCreator(mybatisConfigParse, params).parseTemplate();
			params.put("config", mybatisConfigVmElements);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 生成vo
	 * @param basePath
	 * @param entityVmElements
	 * @param entityInfoVmElements
	 */
	private static void handlerVo(String basePath,VmElement<Entity> entityVmElements , VmElement<EntityInfo> entityInfoVmElements) {
		StringBuilder poBasePath = new StringBuilder(basePath);
		if (! basePath.endsWith(File.separator)) poBasePath.append(File.separator);
		poBasePath.append(entityVmElements.getBaseDir());
		if (! entityVmElements.getBaseDir().endsWith(File.separator)) poBasePath.append(File.separator);

		for (EntityInfo info : entityInfoVmElements.getSubVmElementList()) {
			if (! info.getVo().equals(info.getPoref())) {
				StringBuilder sb = new StringBuilder();
				sb.append(poBasePath).append(entityVmElements.subVmElement(info.getPoref()).getOutFilePath()).append(info.getVo()).append(".java");

				File file = new File(sb.toString());
				if (! file.exists()) {
					EntityVo entityVo = new EntityVo(entityVmElements.subVmElement(info.getPoref()), info.getVo());
					VelocityFactory.getInstance().parseOutFile("vm/entity_vo_create.vm", sb.toString(), entityVo);
				}
			}
		}
	}
}