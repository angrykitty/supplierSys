package com.supplier.blog;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.supplier.common.model.Blog;
import com.jfinal.json.Json;
import com.jfinal.plugin.ehcache.CacheKit;
import com.supplier.tools.K3DbTools;

import java.util.List;

/**
 */
@Before(BlogInterceptor.class)
public class BlogController extends Controller {
	
	BlogService service = BlogService.me;
	
	public void index() {
		setAttr("blogPage", service.paginate(getParaToInt(0, 1), 10));
		render("blog.html");
	}
	
	public void add() {
		String sid = getSession().getId();

		CacheKit.put("LoginUserCache",sid,new Blog().setTitle("test:"+sid).setContent(""+Math.random()));

		renderText("sss::"+sid);
	}


	public void test(){
		String sid = getSession().getId();
		renderText(Json.getJson().toJson(CacheKit.get("LoginUserCache",sid)));

	}


	public void t(){
		List<Record> list = Db.use("db2").query("select * from brand");
		System.out.println(list.size());
	}
	/**
	 * save 与 update 的业务逻辑在实际应用中也应该放在 serivce 之中，
	 * 并要对数据进正确性进行验证，在此仅为了偷懒
	 */
	@Before(BlogValidator.class)
	public void save() {
		getBean(Blog.class).save();
		redirect("/blog");
	}
	
	public void edit() {
		setAttr("blog", service.findById(getParaToInt()));
	}
	
	/**
	 * save 与 update 的业务逻辑在实际应用中也应该放在 serivce 之中，
	 * 并要对数据进正确性进行验证，在此仅为了偷懒
	 */
	@Before(BlogValidator.class)
	public void update() {
		getBean(Blog.class).update();
		redirect("/blog");
	}
	
	public void delete() {
		service.deleteById(getParaToInt());
		redirect("/blog");
	}
}


