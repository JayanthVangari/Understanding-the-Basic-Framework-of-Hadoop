import java.util.Date;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.partition.InputSampler;
import org.apache.hadoop.mapreduce.lib.partition.TotalOrderPartitioner;

public class Hs 
{
	public static void main(String[] args) throws Exception
	{
		int num_of_reducers=1;
		Configuration conf=new Configuration();
		FileSystem fs = FileSystem.get(conf);
		Job job=Job.getInstance(conf);
		job.setJobName("TeraSort");
		job.setJarByClass(Hs.class);
		if(args.length==4)
		{
			
			if(args[0].equals("-r"))
			{
				num_of_reducers=Integer.parseInt(args[1]);
				
			}
		job.setNumReduceTasks(num_of_reducers);
		FileInputFormat.setInputPaths(job,new Path(args[2]));
		FileOutputFormat.setOutputPath(job, new Path(args[3]));
		job.setInputFormatClass(KeyValueTextInputFormat.class);	
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setPartitionerClass(TotalOrderPartitioner.class);
		Path partitionFile = new Path(args[3]+"_partition.lst");	
		TotalOrderPartitioner.setPartitionFile(job.getConfiguration(), partitionFile);
		InputSampler.writePartitionFile(job,
			new InputSampler.RandomSampler(0.1, num_of_reducers));
		job.setMapperClass(Mapr.class);
		job.setReducerClass(Rdcr.class);
		
		Date startTime = new Date();
		System.out.println("          Job started           " );
		int result=job.waitForCompletion(true)?0:1;
		System.out.println("result :	"+result);
		Date end_time = new Date();
		System.out.println("Total Job Time	" + (end_time.getTime() - startTime.getTime()) /1000 + " seconds."); 
		}
		else{
			   System.out.println("bin/hadoop jar /path/Sort.jar -r #num_of_reducers /path/input/#.txt /path/output/#");
		}
	}

	
	}