package org.qiunet.template.parse.xml;

import org.apache.commons.collections.map.HashedMap;
import org.qiunet.project.init.ProjectInitData;
import org.qiunet.utils.string.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  vm 解析的上层, 可以包含若干一样的subvmelement元素,
 *  如果xml本身 没有vmfilePath 和  dirName  filePostfix 则可以自己构建类继承该类, 直接指定.
 * @author qiunet
 *         Created on 16/11/21 07:59.
 */
public abstract class VmElement<T extends SubVmElement> {
	private String baseDir;
	private String vmfilePath;
	private String filePostfix;
	private ProjectInitData initData;
	private List<T> subVmElements = new ArrayList();
	private Map<String, T> subVmElementMap = new HashedMap();

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public void setVmfilePath(String vmfilePath) {
		this.vmfilePath = vmfilePath;
	}

	public void setFilePostfix(String filePostfix) {
		this.filePostfix = filePostfix;
	}

	public String getVmfilePath() {
		return vmfilePath;
	}

	public String getFilePostfix() {
		return filePostfix;
	}

	public void addSubElement(T element) {
		if (StringUtil.isEmpty(element.getName())){
			throw new NullPointerException("element ["+element.getClass().getName()+"]name is empty! ");
		}
		if (! subVmElements.isEmpty()){
			String nowClassName = subVmElements.get(0).getClass().getName();
			String argClassName = element.getClass().getName();
			if(!nowClassName.equals(argClassName))
				throw new IllegalArgumentException("VmElement Child must be same! ["+nowClassName+"] not equals ["+argClassName+"]");
		}
		element.setVmElement(this);
		this.subVmElements.add(element);
		this.subVmElementMap.put(element.getName(), element);
	}
	/**
	 * 根据名称得到一个 SubVmElement
	 * @param name
	 * @return
	 */
	public T subVmElement(String name){
		if (! subVmElementMap.containsKey(name)) {
			throw new NullPointerException("name ["+name+"] is not in Map");
		}
		return subVmElementMap.get(name);
	}
	/***
	 * 得到subElements的list
	 * @return
	 */
	public List<T> getSubVmElementList(){
		return subVmElements;
	}

	ProjectInitData getInitData() {
		return initData;
	}

	public void parseVm(ProjectInitData initData) {
		this.initData = initData;

		String basePath = initData.getConfig().getBasePath();
		if(! basePath.endsWith(File.separator)) basePath += File.separator;
		basePath += this.baseDir;

		if(! basePath.endsWith(File.separator)) basePath += File.separator;

		for(SubVmElement sub : subVmElements) {
			sub.parseOutFile(basePath + sub.getOutFilePath(), getVmfilePath(), getFilePostfix());
		}
	}
}
