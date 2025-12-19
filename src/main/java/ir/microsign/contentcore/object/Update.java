package ir.microsign.contentcore.object;

import ir.microsign.R;
import ir.microsign.context.Application;
import ir.microsign.utility.Text;

/**
 * Created by Mohammad on 2016/08/22.
 */
public class Update extends ir.microsign.dbhelper.object.BaseObject {
	public Integer autoid,id,dbsize,zipsize,ordering,state;
	public String created,desc,	address ,mirror ,suffix;
	@Override
	public String getJsonArrayName() {
		return "offline";
	}
	public String getUrl(){
		return Text.isNullOrEmpty(mirror)? address+"."+suffix:mirror;
	}
	public String getExtractPath(){
		return Application.getContext().getDatabasePath("microsign").getParent()+"/";
	}
	public String getDescription(){
		return String.format(Text.ReadResTxt(Application.getContext(),R.raw.fullupdatechangesformat),desc!=null?desc:"",zipsize/(1024*1024f),dbsize/(1024*1024f));
	}
}
