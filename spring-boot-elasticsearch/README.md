# spring-boot-elasticsearch SpringBoot整合ES搜索引擎
> elasticsearch 是面向文档储存的，可以是数据库中的一个商品、一条订单。<br />
> 文档数据会被序列化为json格式后存储到elasticsearch中</br>
> es擅长海量数据的搜索、分析、计算</br>
> es中没有事务的概念，不能替代数据库,属于和数据库互补的组件

## 基本概念
* 索引（index）：索引是相同类型的文档的集合（相当于MySQL中的表）
* 映射（mapping）：索引中文档的字段约束信息（相当于MySQL的表结构）
* 文档（document）：就是一条条的数据，都是JSON格式（相当于MySQL中的行）
* 字段（Field）：就是文档中的字段（相当于MySQL的列）
* DSL：DSL是elasticsearch提供的JSON风格的请求语句，用来操作elasticsearch，实现CRUD

### 索引类型
* 倒排索引（根据搜索信息查询索引）
  * 对文档内容进行分词，对词条建立索引，并记录词条所在文档的信息。查询时先根据词条获取文档ID，然后再获取对应文档
* 正向索引（MySQL：根据索引查询数据）
  * 基于文档（数据）ID创建索引，查询词条时必需先找到文档，然后判断是否包含词条

### mapping属性
mapping属性是对索引库中文档的约束，常见的mapping属性包括：
* type：字段数据类型，常见的简单类型有：
  * 字符串：text（可分词的文本）、keyword（精确值，例如品牌名称、国家、ip地址）
  * 数值：long、integer、short、byte、double、float
  * 布尔：boolean
  * 日期：date
  * 对象：object
* index：是否创建索引，默认为true
* analyzer：使用哪种分词器
* properties：该字段的子字段

## 分词器
作用
* 创建倒排索引时对文档进行分词
* 用户搜索时，对输入的内容进行分词

### 安装中文分词器（ik分词器）
***7.17.4改为对应的es版本***
```bash
./bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.17.4/elasticsearch-analysis-ik-7.17.4.zip
```

**ik分词器包含两种模式**
* ik_smart：最少拆分
* ik_max_work：最细拆分

### ik分词器扩展
修改配置
```bash
elasticsearch/analysis-ik/IKAnalyzer.cfg.xml
```
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
	<comment>IK Analyzer 扩展配置</comment>
    <!-- ext.dic和stopword.dic是自己创建的扩展字典 -->
	<!--用户可以在这里配置自己的扩展字典 -->
	<entry key="ext_dict">ext.dic</entry>
	 <!--用户可以在这里配置自己的扩展停止词字典-->
	<entry key="ext_stopwords">stopword.dic</entry>
	<!--用户可以在这里配置远程扩展字典 -->
	<!-- <entry key="remote_ext_dict">words_location</entry> -->
	<!--用户可以在这里配置远程扩展停止词字典-->
	<!-- <entry key="remote_ext_stopwords">words_location</entry> -->
</properties>
```
***mac上修改了ik的配置后重启es时报这个错：***
<font size=1>Exception in thread "main" java.nio.file.NotDirectoryException: /opt/homebrew/Cellar/elasticsearch-full/7.17.4/libexec/plugins/.DS_Store</font>
**解决方法：删除.DS_Store**

测试分词器：
```json
# 英语分词器
post /_analyze
{
"text":"废物刀Dog头开机油桶",
"analyzer":"english"
}

# 标准分词器(只支持英文)
post /_analyze
{
"text":"废物刀Dog头开机油桶",
"analyzer":"standard"
}

# ik_smart
post /_analyze
{
"text":"废物刀Dog头开机油桶",
"analyzer":"ik_smart"
}

# ik_max_word
post /_analyze
{
"text":"废物刀Dog头开机油桶",
"analyzer":"ik_max_word"
}
```

## 索引库操作
* 创建索引库：put /索引库名
* 查询索引库：get /索引库名
* 删除索引库：delete /索引库名
* 添加字段：put /索引库名/_mapping

测试操作索引库
```json
# 查询所有索引库情况
get _cat/indices?v&pretty

# 创建索引库
put /mytest
{
  "mappings":{
    "properties":{
      "info":{
        "type":"text",
        "analyzer":"ik_smart"
      },
      "email":{
        "type":"keyword",
        "index":false
      },
      "name":{
        "type": "object",
        "properties":{
          "firstName":{
            "type":"keyword"
          },
          "lastName":{
            "type":"keyword"
          }
          
        }
      }
    }
  }
}

# 查询索引库信息
get /mytest

# 删除索引库
delete /mytest

# 索引库和mapping一旦创建就无法修改，但可以添加新字段
put /mytest/_mapping
{
  "properties":{
    "age":{
      "type":"integer"
    }
  }
}
```

## 文档操作

* 创建文档：post /索引库名/_doc/文档id {json文档}
* 查询文档：get /索引库名/_doc/文档id
* 删除文档：delete /索引库名/_doc/文档id
* 修改文档：
  * 全量修改：put /索引库名/_doc/文档id {json文档}
  * 增量修改：post /索引库名/_update/文档id {"doc":{字段}}

测试文档操作
```json
# 插入文档
post /mytest/_doc/1
{
  "info":"废物刀Dog头开机油桶",
  "emain":"2587403163@qq.com",
  "age":"1",
  "name":{
    "firstName":"大锤",
    "lastName":"王"
  }
}

# 查询文档
get /mytest/_doc/1

# 删除文档
delete /mytest/_doc/1

# 全量修改文档（直接覆盖）
put /mytest/_doc/1
{
  "info":"废物刀Dog头开机油桶",
  "emain":"2587403163@qq.com",
  "age":"1",
  "name":{
    "firstName":"小锤",
    "lastName":"王"
  }
}

# 局部修改文档
post /mytest/_update/1
{
  "doc":{
    "name":{
      "firstName":"大锤"
    }
  }
}
```
