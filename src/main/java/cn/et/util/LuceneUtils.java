package cn.et.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.TextFragment;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class LuceneUtils {

    // 索引库的位置
    private static String dir = "E:/index";
    // 定义分词器
    private static Analyzer analyzer = new IKAnalyzer();

    
    public  static List<Map<String, String>> search(String field,String content) throws Exception {

        Directory directory = FSDirectory.open(new File(dir));
      //读取索引库的存储目录
        DirectoryReader ireader = DirectoryReader.open(directory);
        // 搜索类
        IndexSearcher isearcher = new IndexSearcher(ireader);
        //lucence查询解析 用于指定查询的属性名和分词器
        QueryParser parser = new QueryParser(Version.LUCENE_47, field, analyzer);
        // 开始搜索
        Query query = parser.parse(content);
        //最终结果被分词后添加前缀和后缀的处理类默认是<B></B> 
        SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<font color=red>","</font>");
        Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(query));
        
        
        // 获取搜索的结果 指定返回的docuemnt个数
        ScoreDoc[] hits = isearcher.search(query, null, 10).scoreDocs;
        
        List<Map<String, String>> list=new ArrayList<Map<String,String>>();
        
        
        //遍历查询出来的document
        for (int i = 0; i < hits.length; i++) {
            int id=hits[i].doc;
            Document hitDoc = isearcher.doc(hits[i].doc);
            Map<String, String> map=new HashMap<String, String>();
            map.put("foodid", hitDoc.get("foodid"));
            String foodName=hitDoc.get("foodName");
            TokenStream tokenStream = TokenSources.getAnyTokenStream(isearcher.getIndexReader(), id, "foodName", analyzer);            
            //输入的第二个参数是查询的值
            TextFragment[] frag = highlighter.getBestTextFragments(tokenStream, foodName, false, 200);
            for (int j = 0; j < frag.length; j++) {
              if ((frag[j] != null) && (frag[j].getScore() > 0)) {
                foodName=frag[j].toString();
              }
            }
            //将经过高亮处理的数据添加至返回结果
            map.put("foodName",foodName );
            map.put("price", hitDoc.get("price"));
            list.add(map);
        }
        // 关闭通道
        ireader.close();
        directory.close();
        return list;
    }
    

    public static void write(Document doc) throws Exception {
        // 索引库的存放目录
        Directory directory = FSDirectory.open(new File(dir));
        // 关联Lucene版本和当前分词器
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
        // 传入目录和分词器
        IndexWriter iwriter = new IndexWriter(directory, config);

        // 写入document
        iwriter.addDocument(doc);
        // 提交
        iwriter.commit();
        // 关闭通道
        iwriter.close();
    }
}
