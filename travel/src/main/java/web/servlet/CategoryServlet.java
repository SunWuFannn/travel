package web.servlet;

import domain.Category;
import service.CategoryService;
import service.Impl.CategoryServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/category/*")
public class CategoryServlet extends BaseServlet {

    private CategoryService categoryService = new CategoryServiceImpl();

    /**
     * 查询所有分类
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void findAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //调用service查询所有数据
        List<Category> categoryList = categoryService.findAll();
        //序列化返回json
        writeValue(categoryList, resp);
    }
}
