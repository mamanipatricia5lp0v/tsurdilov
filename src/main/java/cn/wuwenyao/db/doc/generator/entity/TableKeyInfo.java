package cn.wuwenyao.db.doc.generator.entity;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

/***
 * 表的索引信息
 * 
 * @author wwy
 *
 */
public class TableKeyInfo {

	/***
	 * 索引名称
	 */
	private String name;

	/***
	 * 包含那些字段
	 */
	private List<String> columns;

	/***
	 * 是否唯一
	 */
	private Boolean unique;

	/***
	 * 索引类型
	 */
	private String indexType;

	/***
	 * 索引注释
	 */
	private String indexComment;

	public TableKeyInfo() {

	}

	public String getColumnCombine() {
		return StringUtils.join(columns, ",");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public Boolean getUnique() {
		return unique;
	}

	public void setUnique(Boolean unique) {
		this.unique = unique;
	}

	public String getIndexType() {
		return indexType;
	}

	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}

	public String getIndexComment() {
		return indexComment;
	}

	public void setIndexComment(String indexComment) {
		this.indexComment = indexComment;
	}

}
