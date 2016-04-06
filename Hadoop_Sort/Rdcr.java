import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class Rdcr extends Reducer<Text,Text,Text,Text>{

	@Override
	public void reduce(Text arg0, Iterable<Text> arg1,
			Context context) throws IOException, InterruptedException {
			for( Text t:arg1)			
			{
				context.write(arg0,t);
			}
	}
	
}
