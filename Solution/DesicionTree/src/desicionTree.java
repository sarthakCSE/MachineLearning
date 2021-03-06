import java.util.*;
import java.io.*;


class treeNodes{
	
	double e;
	double infoGain;
	boolean marked;	
	String targetAttribute;
	treeNodes leftSide;
	treeNodes rightSide;
	int zerocount;
	int onecount;
	int value;
	String classify;
	HashMap<Integer,String> attributes;
	HashSet<Integer> indexes;

	treeNodes(treeNodes node){


	this.e=node.e;
	this.infoGain=node.infoGain;
	this.marked=node.marked;	
	this.targetAttribute=node.targetAttribute;
	this.zerocount=node.zerocount;
	this.onecount=node.onecount;
	this.value=node.value;
	this.classify=node.classify;
	this.attributes=new HashMap<Integer,String>(node.attributes);
	this.indexes=new HashSet<Integer>(node.indexes);

	}

	treeNodes(){

		value=-1;
		e=0;
		infoGain=0;
		leftSide=null;
		marked=false;

		rightSide=null;
		zerocount=0;
		onecount=0;
		classify="";
		attributes=new HashMap<Integer,String>();
		indexes=new HashSet<Integer>();
		targetAttribute="";

	}

	void setmarked(boolean val)
	{
		marked=val;
	}

	boolean ismarked()
	{
		return marked;
	}

	void setIndexes(HashSet<Integer> index)
	{
		indexes=new HashSet<Integer>(index);
	}

	HashSet<Integer> getIndexes()
	{
		return indexes;
	}

	void getIndex()
	{
		Iterator itr=indexes.iterator();
		System.out.println("Index Set size="+indexes.size());
		while(itr.hasNext())
		{
			System.out.print((Integer)itr.next()+" ");
		}
	}

	void setAttributes(HashMap<Integer,String> attrName)
	{
		attributes.putAll(attrName);
	}

	HashMap<Integer,String> getAttributes()
	{
		return attributes;
	}

	String getAttributeValue(int key)
	{
		return attributes.get(key);
	}

	boolean checkAttribute()
	{
		if((attributes.size()-1)>0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	void setvalue(int val)
	{
		value=val;
	}

	int getvalue()
	{
		return value;
	}

	String getclassify()
	{
		return classify;
	}

	String getTargetAttribute()
	{
		return targetAttribute;
	}


	void setclassify(String attribute)
	{
		classify=attribute;
	}

	int getCounts(int value)
	{
		if(value==0)
			return zerocount; 
		else
			return onecount;		
	}

	void removeAttribute(int attr)
	{
		attributes.remove(attr);
	}


	void showAttributes()
	{
		Iterator itr=attributes.entrySet().iterator();
		System.out.println("No. of Attributes in this node:"+attributes.size());

		while(itr.hasNext()){

			Map.Entry pair=(Map.Entry)itr.next();
			System.out.println(pair.getKey()+":"+pair.getValue());
		}
	}


	void setTargetAttribute(String target)
	{
		targetAttribute=target;
	}


	void rootCount(HashMap<Integer,HashMap<String,Integer>> records){

		zerocount=onecount=0;

		Iterator itr=records.entrySet().iterator();

		HashMap<String,Integer> innerMap;
	
		while(itr.hasNext()){

			Map.Entry pair=(Map.Entry) itr.next();			

			innerMap=new HashMap<String,Integer>();

			int rowNo=(Integer)pair.getKey();

			innerMap=(HashMap<String,Integer>)pair.getValue();

			indexes.add(rowNo);
		
			if(innerMap.get(getTargetAttribute())==0){

				zerocount++;
			}
			else{

				onecount++;
			}

		}

	}


	void uniqueCounts(HashMap<Integer,HashMap<String,Integer>> records,String attr, int value){

		zerocount=onecount=0;

		Iterator itr=getIndexes().iterator();

		HashSet<Integer> newSet=new HashSet<Integer>();

		HashMap<String,Integer> temp;

		while(itr.hasNext()){

			int rowNo=(Integer)itr.next();

			temp=new HashMap<String,Integer>(records.get(rowNo));


			if(temp.get(attr)==value){

				newSet.add(rowNo);

				if(temp.get(getTargetAttribute())==0) {

					zerocount++;
				}
				else{

					onecount++;
				}
			
			}

		}

		indexes.clear();
		indexes=new HashSet<Integer>(newSet);
	}

	void calculateEntropy(){

		int count=(zerocount+onecount);
		
		if(count==0){
			e=0;
		}
		else{
			double p1=(double)zerocount/count;
			double p2=(double)onecount/count;
			double e1=(p1*(log(p1,2))*-1);
			double ent2=(p2*(log(p2,2))*-1);

			e=(double)e1+ent2;
		}
	}


	static double log(double x, int base){
		if(x!=0)
    		return (Math.log(x) / Math.log(base));
    	else
    		return 0;
	}

	void calculateGain(){

		if(leftSide==null && rightSide==null){

 			infoGain=1.0;

		}	
		else{

			double entropy1;
			double entropy2;

			leftSide.calculateEntropy();
			entropy1=leftSide.getEntropy();

			rightSide.calculateEntropy();
			entropy2=rightSide.getEntropy();

			int totalleftSide=leftSide.getCounts(0)+leftSide.getCounts(1);
			int totalrightSide=rightSide.getCounts(0)+rightSide.getCounts(1);

			calculateEntropy();

			int total=zerocount+onecount;
			double probleftSide=(double)totalleftSide/total;
			double probrightSide=(double)totalrightSide/total;
			double ent2= (double)probleftSide* entropy1;
			double product2= (double)probrightSide* entropy2;
			infoGain=(double)e-(ent2+product2);

		}
	}

	treeNodes getleftSideNode(){

		return leftSide;
	}

	treeNodes getrightSideNode(){

		return rightSide;
	}

	void setleftSideNode(treeNodes node){

		leftSide=node;
	}


	void setrightSideNode(treeNodes node){

		rightSide=node;
	}


	void setEntropy(double value){

		e=value;
	}


	void setInfoGain(double value){

		infoGain=value;
	}


	double getInfoGain(){

		return infoGain;
	}
	
	double getEntropy(){

		return e;
	}
}

class decisionTree
{

	treeNodes root;	
	LinkedList<treeNodes> nodeList;
	int size;

	decisionTree(){

		root=null;
		size=0;
	}

	decisionTree(decisionTree orig)
	{
		root=new treeNodes(orig.getRoot());

		root.setleftSideNode(null);
		root.setrightSideNode(null);

		nodeList=new LinkedList<treeNodes>(orig.nodeList);

		copytreeNodess(orig.getRoot(),root);
	}

	void copytreeNodess(treeNodes origNode, treeNodes currNode){

		if(origNode==null || currNode==null){

			return;
		}

		if(origNode.getleftSideNode()!=null){

			treeNodes newNode=new treeNodes(origNode.getleftSideNode());	
			currNode.setleftSideNode(newNode);
		}


		if(origNode.getrightSideNode()!=null){

			treeNodes newNode=new treeNodes(origNode.getrightSideNode());	
			currNode.setrightSideNode(newNode);
		}

		copytreeNodess(origNode.getleftSideNode(),currNode.getleftSideNode());

		copytreeNodess(origNode.getrightSideNode(),currNode.getrightSideNode());
		
	}

	decisionTree(createInstance trainingSet){
	
	nodeList=new LinkedList<treeNodes>();
	root=null;
	size=0;

	root=new treeNodes();
	root.setAttributes(trainingSet.getattrName());
	HashMap<Integer,String> temp=root.getAttributes();	
	root.setTargetAttribute(temp.get(temp.size()-1));
	root.rootCount(trainingSet.getRecords());

	root.calculateEntropy();

	if(trainingSet.getSize()==root.getCounts(1))
	{
		root.setclassify("Root");
		root.setvalue(1);
		return;
	}

	if(trainingSet.getSize()==root.getCounts(0)){
		root.setclassify("Root");
		root.setvalue(0);
		return;
	}

	if(root.checkAttribute())
	{		
		if(root.getCounts(0)>root.getCounts(1))
		{
			root.setclassify("Root");
			root.setvalue(0);
		}
		else
		{
			root.setclassify("Root");
			root.setvalue(1);
		}
		return;
	}

	root.setleftSideNode(null);
	root.setrightSideNode(null);

	buildTree(trainingSet,root);	
	}
	
	void display(treeNodes node,int level)
	{
		if(node==null)
		{
			return;
		}

		level++;

		if(node.getleftSideNode()!=null){
			
			treeNodes leftSide=node.getleftSideNode();

			int i=0;

			while(i<level){

				System.out.print("| ");
				i++;
			}

			if(leftSide.getvalue()==-1){
				
				System.out.println(node.getclassify()+" = 0 :");
			}
			else{

				System.out.println(node.getclassify()+" = 0 : "+leftSide.getvalue());
			}

				display(leftSide,level);
		}


		if(node.getrightSideNode()!=null){
			
			treeNodes rightSide=node.getrightSideNode();

			int i=0;

			while(i<level){

				System.out.print("| ");
				i++;
			}

			if(rightSide.getvalue()==-1){
				
				System.out.println(node.getclassify()+" = 1 :");
			}
			else{

				System.out.println(node.getclassify()+" = 1 : "+rightSide.getvalue());
			}
		

			display(rightSide,level);

		}

	}

	void buildTree(createInstance trainingSet, treeNodes node){
	
		if(node==null){

			return;
		}

		if(node.getCounts(0)==(node.getCounts(0)+node.getCounts(1))){

				node.setvalue(0);
				return;
		}

		if(node.getCounts(1)==(node.getCounts(0)+node.getCounts(1))){

			node.setvalue(1);
			return;
		}

		if(node.checkAttribute())
		{			
			if(node.getCounts(0)>node.getCounts(1))
			{
				node.setvalue(0);
			}
			else
			{
				node.setvalue(1);	
			}			
			return;
		}		

		int attributeValue=bestAttribute(trainingSet,node);
		node.setclassify(node.getAttributeValue(attributeValue));
		node.calculateEntropy();
		HashMap<Integer,String> tempNode=new HashMap<Integer,String>();
		tempNode.putAll(node.getAttributes());		
		treeNodes leftSide=new treeNodes();
		leftSide.setAttributes(tempNode);	
		leftSide.setTargetAttribute(node.getTargetAttribute());
		leftSide.setIndexes(node.getIndexes());
		leftSide.uniqueCounts(trainingSet.getRecords(), leftSide.getAttributeValue(attributeValue), 0);
		leftSide.calculateEntropy();
		leftSide.removeAttribute(attributeValue);		
		treeNodes rightSide=new treeNodes();
		rightSide.setAttributes(tempNode);
		rightSide.setTargetAttribute(node.getTargetAttribute());
		rightSide.setIndexes(node.getIndexes());
		rightSide.uniqueCounts(trainingSet.getRecords(), rightSide.getAttributeValue(attributeValue), 1);
		rightSide.calculateEntropy();
		rightSide.removeAttribute(attributeValue);
		if(leftSide.getIndexes().size()>0)
		{	
			node.setleftSideNode(leftSide);
			buildTree(trainingSet,leftSide);	
		}		
		
		if(rightSide.getIndexes().size()>0)
		{	
			node.setrightSideNode(rightSide);
			buildTree(trainingSet,rightSide);	
		}
	}
	int bestAttribute(createInstance instance, treeNodes node)
	{
		double max=Double.NEGATIVE_INFINITY;
		int maxGain=-1, key=-1;
		String temp="";
		Iterator itr=sortByValues(node.getAttributes()).entrySet().iterator();
		while(itr.hasNext())
		{
			Map.Entry pair=(Map.Entry)itr.next();
			key=(Integer)pair.getKey();
			String value=(String)pair.getValue();
			if(node.getTargetAttribute()!=value){
				double k=buildGain(instance,value,node);
				if(k>max)
				{
					max=k;
					maxGain=key;
					temp=value;
				}
			}
		}

	node.setleftSideNode(null);
	node.setrightSideNode(null);
	node.setInfoGain(max);

	return maxGain;
} 

	double buildGain(createInstance instance, String attributeValue,treeNodes node)
	{
		treeNodes leftSide=new treeNodes();
		leftSide.setTargetAttribute(node.getTargetAttribute());
		leftSide.setIndexes(node.getIndexes());
		leftSide.uniqueCounts(instance.getRecords(), attributeValue,0);
		leftSide.calculateEntropy();
		treeNodes rightSide=new treeNodes();
		rightSide.setTargetAttribute(node.getTargetAttribute());
		rightSide.setIndexes(node.getIndexes());
		rightSide.uniqueCounts(instance.getRecords(), attributeValue,1);
		rightSide.calculateEntropy();
		node.setleftSideNode(leftSide);
		node.setrightSideNode(rightSide);
		node.calculateGain();
		return node.getInfoGain();
	}
	
	int getSize()
	{
		return size;
	}
	
	treeNodes getRoot()
	{
		return root;
	}

public static void main(String[] args) throws IOException
{
	int L= Integer.parseInt(args[0]);
	int K=Integer.parseInt(args[1]);	
	String training_set=args[2];
	String validation_set=args[3];
	String test_set=args[4];
	String choice=args[5];

	createInstance trainingSet=new createInstance(training_set);
	decisionTree tree=new decisionTree(trainingSet);
	createInstance validationSet=new createInstance(validation_set);
	createInstance testingSet=new createInstance(test_set);		
	double accuracy=(double) Math.round(tree.accuracy(testingSet)*10000)/100;
	System.out.println("Accuracy of decision tree before pruning="+accuracy+"%");
	decisionTree treeAfterPruning=tree.prune(L,K,validationSet);
	accuracy=(double) Math.round(treeAfterPruning.accuracy(testingSet)*10000)/100;
	System.out.println("Accuracy of decision tree after pruning ="+accuracy+"%");
	if(choice.equals("yes"))
	{		
		System.out.println("-------------------------------");
		System.out.println("-------DECISION TREE-----------");
		System.out.println("-------------------------------");
		treeAfterPruning.display(treeAfterPruning.getRoot(),-1);
	}
	}
	double accuracy(createInstance instances)
	{
		Iterator itr=instances.getRecords().entrySet().iterator();
		HashMap<String,Integer> temp;
		int nextCounter=0, total=0;
		while(itr.hasNext())
		{
			Map.Entry entry=(Map.Entry) itr.next();
			temp=new HashMap<String,Integer>((HashMap<String,Integer>)entry.getValue());
			int predictedVal=predictValue(root,temp);
			int actualVal=temp.get(root.getTargetAttribute());
			if(predictedVal==actualVal)
			{
				nextCounter++;
			}

			total++;
		}
		return (double)nextCounter/total;
		}


	int predictValue(treeNodes node, HashMap<String,Integer> temp)
	{
		if(node==null)
			{
				return -1;
			}
			if(node.getleftSideNode()==null && node.getrightSideNode()==null)
			{
				return node.getvalue();
			}

			if(temp.get(node.getclassify())==0)
			{
				if(node.getleftSideNode()!=null)
				{
					return predictValue(node.getleftSideNode(), temp);
				}				
			}
			if(temp.get(node.getclassify())==1)
			{
				if(node.getrightSideNode()!=null)
				{
					return predictValue(node.getrightSideNode(), temp);
				}	
			}
			return -1;
	}

		void initList()
			{
				nodeList=new LinkedList<treeNodes>();
			}

	decisionTree prune(int L, int K, createInstance instances)
	{
	decisionTree bestTree=new decisionTree(this);
	double bestAccuracy=bestTree.accuracy(instances);
	double accuracy=0;
	for(int i=1;i<=L;i++)
	{
		decisionTree currentTree=new decisionTree(this);
		Random randomValue=new Random();
		int M=randomValue.nextInt(K)+1;

		for(int j=1;j<=M;j++){

			LinkedList<treeNodes> list=new LinkedList<treeNodes>();
			
			list.add(currentTree.getRoot());
			currentTree.initList();
			currentTree.levels(list);
			int N=currentTree.nodeList.size();
			Random randomValue1=new Random();
			int P=randomValue1.nextInt(N);			
			if(P==0)
			{
				continue;
			}
			if(!currentTree.nodeList.get(P).marked)
			{
				treeNodes node=currentTree.nodeList.get(P);
				treeNodes newNode=new treeNodes();
				node.setmarked(true);
				node.setleftSideNode(null);
				node.setrightSideNode(null);				
				node.setleftSideNode(newNode);
				if(node.getCounts(0)>node.getCounts(1))
				{
					newNode.setvalue(0);
				}
				else
				{
					newNode.setvalue(1);
				}
			}
			else
			{
				continue;
			}
		}
		accuracy=currentTree.accuracy(instances);
		if(accuracy>bestAccuracy)
		{
			bestAccuracy=accuracy;
			bestTree=new decisionTree(currentTree);
		}
	}
return bestTree;
}
void displayList()
{
	for(int i=0;i<nodeList.size();i++)
	{
		if(i==nodeList.size()-1)
			System.out.print(nodeList.get(i).getCounts(0)+":"+nodeList.get(i).getCounts(1));
		else	
			System.out.print(nodeList.get(i).getCounts(0)+":"+nodeList.get(i).getCounts(1)+"->");
	}
	System.out.println(" ");
}

private static HashMap sortByValues(HashMap map)
{
       List list = new LinkedList(map.entrySet());
       // Defined Custom Comparator here
       Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
               return ((Comparable) ((Map.Entry) (o1)).getValue())
                  .compareTo(((Map.Entry) (o2)).getValue());
            }
       });
       
       HashMap sortedHashMap = new LinkedHashMap();
       for (Iterator it = list.iterator(); it.hasNext();) {
              Map.Entry entry = (Map.Entry) it.next();
              sortedHashMap.put(entry.getKey(), entry.getValue());
       } 
       return sortedHashMap;
  }


	void levels(LinkedList<treeNodes> list)
	{
		if(list.size()==0)
		{
			return;
		}
		LinkedList<treeNodes> newList=new LinkedList<treeNodes>();
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).getleftSideNode()!=null && list.get(i).getrightSideNode()!=null)
			{
				if(list.get(i).getleftSideNode()!=null)
				{
					newList.addLast(list.get(i).getleftSideNode());
				}				
				if(list.get(i).getrightSideNode()!=null)
				{
					newList.addLast(list.get(i).getrightSideNode());
				}
				nodeList.addLast(list.get(i));
					
			}
			else
			{
				continue;
			}
		}
		levels(newList);
	}
}
class createInstance
{
	HashMap<Integer,String> attrName;
	int size;
	HashMap<Integer,HashMap<String,Integer>> records;

	createInstance(){
		attrName=new HashMap<Integer,String>();
		records=new HashMap<Integer,HashMap<String,Integer>>();		
		size=0;
	}

	HashMap<Integer,HashMap<String,Integer>> getRecords()
	{
		return records;
	}

	HashMap<Integer,String> getattrName()
	{
		return attrName;
	}

	int getSize(){

		return size;
	}

	createInstance(String fileLocation) throws IOException{

		BufferedReader reader=new BufferedReader(new FileReader(fileLocation));
		size=0;
		String line="";
		int i=0;
		attrName=new HashMap<Integer,String>();
		records=new HashMap<Integer,HashMap<String,Integer>>();
		boolean first=true;

		while((line=reader.readLine())!=null && line.length()!=0){

			if(first == false)
			{
				addRecord(line,i);
				size++;
				i++;				
			}
			else
			{
				addAtrribute(line);
				first=false;
			}
		}
		reader.close();
	}
	
	void addAtrribute(String line)
	{
		String[] array=fileSplit(line);
		for(int i=0;i<array.length;i++)
		{
			attrName.put(i,array[i]);
		}
	}
	
	void addRecord(String line,int row)
	{
		HashMap<String,Integer> attribute;
		attribute=new HashMap<String,Integer>();

		for(int j=0,i=0;i<attrName.size();j=j+2,i++)
		{
			attribute.put(attrName.get(i),Character.getNumericValue(line.charAt(j)));
		}
		records.put(row,attribute);
	}	

	String[] fileSplit(String line)
	{
		return line.split(",");
	}
}