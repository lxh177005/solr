package com.xingxin.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class TestSolr {
    //product核的url
    private static final String solrUrl="http://localhost:8983/solr/product";
    private SolrClient solrClient;

    @Before
    public void before(){
        //连接solr客户端
        HttpSolrClient.Builder builder = new HttpSolrClient.Builder(solrUrl);
        solrClient = builder.build();
    }

    //添加到索引库
    @Test
    public void addDocument() throws IOException, SolrServerException {
        //创建document对象
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id","21");
        document.addField("productName","中华上下五千年");
        //_t是分词的动态域
        document.addField("product_des_t","中华小当家很可怕！！！");
        //把document对象添加到索引库里
        solrClient.add(document); //注意：id存在就修改 id不存在才添加
    }

    //从索引库中删除
    @Test
    public void deleteDocument() throws IOException, SolrServerException {
        solrClient.deleteById("21");
    }

    //查询
    @Test
    public void query() throws IOException, SolrServerException {
        //创建查询对象
        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件
        solrQuery.set("q","*:*");//键是q ，查询所有*：*
//        solrQuery.set("q","productName:鸡蛋");
        //设置分页查询
//        int pageNo=1;
//        int pageSize=2;
//        solrQuery.set("start",(pageNo-1)*pageSize);
//        solrQuery.set("rows",pageSize);
        //只查询某些域
//        solrQuery.set("fl","productName:鸡蛋");
        //过滤条件
//        solrQuery.set("fq","productPrice:[0 TO 20]");
        //指定排序规则 desc降序 asc升序
        solrQuery.setSort("productPrice",SolrQuery.ORDER.desc);

        //开启高亮
        solrQuery.setHighlight(true);
        //指定高亮前缀
        solrQuery.setHighlightSimplePre("<span style='color:red;'>");
        //指定高亮的结束
        solrQuery.setHighlightSimplePost("</span>");
        //指定高亮的域
        solrQuery.addHighlightField("productName");

        //获取查询结果
        QueryResponse queryResponse = solrClient.query(solrQuery);
        SolrDocumentList results = queryResponse.getResults();
        System.out.println("NumFound：" + results.getNumFound());
        for (SolrDocument result : results) {
            System.out.println("id:"+result.get("id"));
            System.out.println("productName:"+result.get("productName"));
            System.out.println("productDescrip:"+result.get("productDescrip"));
            System.out.println("productPrice:"+result.get("productPrice"));
            System.out.println("--------------------------------");
        }
    }
    @After
    public void after() throws IOException, SolrServerException {
        //提交
        solrClient.commit();
    }
}
