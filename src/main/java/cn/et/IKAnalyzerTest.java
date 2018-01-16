package cn.et;

import java.io.IOException;  
import java.io.StringReader;  
  
import org.apache.lucene.analysis.TokenStream;  
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;  
import org.wltea.analyzer.core.IKSegmenter;  
import org.wltea.analyzer.core.Lexeme;  
import org.wltea.analyzer.lucene.IKAnalyzer;  

/**
 * IKAnalyzer �ִ�������
 * 
 * @author Luxh
 */
public class IKAnalyzerTest {

    public static void main(String[] args) throws Exception {  
        demo();  
    }  
  
    // �ִʲ���  
    public static void demo() throws Exception {  
        @SuppressWarnings("resource")
        IKAnalyzer analyzer = new IKAnalyzer(true);  
        System.out.println("��ǰʹ�õķִ�����" + analyzer.getClass().getSimpleName());  
        TokenStream tokenStream = analyzer.tokenStream("content", "������������ ϲ����ѪѼ");  
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);  
        tokenStream.reset();// �����ȵ���reset����  
        while (tokenStream.incrementToken()) {  
            System.out.println(new String(charTermAttribute.buffer()));  
        }  
        tokenStream.close();  
    }  
  
    // ������ʹ��IKAnalyzer������ֱ��ʹ��IK�ִ����ĺ����࣬�����ִʵ�ʵ����IKSegmenter�ִ������зִ�  
    public static void demo1() throws Exception {  
        StringReader reader = new StringReader("������������ ϲ����ѪѼ");  
        IKSegmenter ik = new IKSegmenter(reader, true);// ��Ϊtrueʱ���ִ����������ʳ��з�  
        Lexeme lexeme = null;  
        try {  
            while ((lexeme = ik.next()) != null)  
                System.out.println(lexeme.getLexemeText());  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            reader.close();  
        }  
    }  
}