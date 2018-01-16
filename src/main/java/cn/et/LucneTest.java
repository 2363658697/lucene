package cn.et;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class LucneTest {
    // 索引库的位置
    private static String dir = "E:/index";
    // 定义分词器
    private static Analyzer analyzer = new IKAnalyzer();

    public static void main(String[] args) throws Exception {
        search();
    }



    public static void write() throws IOException {
        // 索引库的存放目录
        Directory directory = FSDirectory.open(new File(dir));
        // 关联Lucene版本和当前分词器
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
        // 传入目录和分词器
        IndexWriter iwriter = new IndexWriter(directory, config);
        // 创建document和field（对象，属性）
        Document doc = new Document();
        Field field = new Field("userName", "张三", TextField.TYPE_STORED);
        // document���field
        doc.add(field);
        field = new Field("userDesc", "张三来自永州 喜欢吃血鸭", TextField.TYPE_STORED);
        doc.add(field);

        Document doc1 = new Document();
        Field field1 = new Field("userName", "李四", TextField.TYPE_STORED);
        doc1.add(field1);
        field1 = new Field("userDesc", "李四来自兰州 喜欢拉拉面", TextField.TYPE_STORED);
        doc1.add(field1);

        // 写入document
        iwriter.addDocument(doc);
        iwriter.addDocument(doc1);
        // 提交
        iwriter.commit();
        //关闭通道
        iwriter.close();
    }

    public static void search() throws Exception {

        Directory directory = FSDirectory.open(new File(dir));
      //读取索引库的存储目录
        DirectoryReader ireader = DirectoryReader.open(directory);
        // 搜索类
        IndexSearcher isearcher = new IndexSearcher(ireader);
        //lucence查询解析 用于指定查询的属性名和分词器
        QueryParser parser = new QueryParser(Version.LUCENE_47, "userDesc", analyzer);
        // 开始搜索
        Query query = parser.parse("来");
        // 获取搜索的结果 指定返回的docuemnt个数
        ScoreDoc[] hits = isearcher.search(query, null, 2).scoreDocs;
        //遍历查询出来的document
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = isearcher.doc(hits[i].doc);
            System.out.println(hitDoc.getField("userName").stringValue());
        }
        // 关闭通道
        ireader.close();
        directory.close();
    }
}
