/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.47
 * Generated at: 2018-05-14 13:57:28 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.common;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class page_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n");
      out.write("\n");
      out.write("<script id=\"paginateTemplate\" type=\"x-tmpl-mustache\">\n");
      out.write("<div class=\"col-xs-6\">\n");
      out.write("    <div class=\"dataTables_info\" id=\"dynamic-table_info\" role=\"status\" aria-live=\"polite\">\n");
      out.write("        总共 {{total}} 中的 {{from}} ~ {{to}}\n");
      out.write("    </div>\n");
      out.write("</div>\n");
      out.write("    \n");
      out.write("<div class=\"col-xs-6\">\n");
      out.write("    <div class=\"dataTables_paginate paging_simple_numbers\" id=\"dynamic-table_paginate\">\n");
      out.write("        <ul class=\"pagination\">\n");
      out.write("            <li class=\"paginate_button previous {{^firstUrl}}disabled{{/firstUrl}}\" aria-controls=\"dynamic-table\" tabindex=\"0\">\n");
      out.write("                <a href=\"#\" data-target=\"1\" data-url=\"{{firstUrl}}\" class=\"page-action\">首页</a>\n");
      out.write("            </li>\n");
      out.write("            <li class=\"paginate_button {{^beforeUrl}}disabled{{/beforeUrl}}\" aria-controls=\"dynamic-table\" tabindex=\"0\">\n");
      out.write("                <a href=\"#\" data-target=\"{{beforePageNo}}\" data-url=\"{{beforeUrl}}\" class=\"page-action\">前一页</a>\n");
      out.write("            </li>\n");
      out.write("            <li class=\"paginate_button active\" aria-controls=\"dynamic-table\" tabindex=\"0\">\n");
      out.write("                <a href=\"#\" data-id=\"{{pageNo}}\" >第{{pageNo}}页</a>\n");
      out.write("                <input type=\"hidden\" class=\"pageNo\" value=\"{{pageNo}}\" />\n");
      out.write("            </li>\n");
      out.write("            <li class=\"paginate_button {{^nextUrl}}disabled{{/nextUrl}}\" aria-controls=\"dynamic-table\" tabindex=\"0\">\n");
      out.write("                <a href=\"#\" data-target=\"{{nextPageNo}}\" data-url=\"{{nextUrl}}\" class=\"page-action\">后一页</a>\n");
      out.write("            </li>\n");
      out.write("            <li class=\"paginate_button next {{^lastUrl}}disabled{{/lastUrl}}\" aria-controls=\"dynamic-table\" tabindex=\"0\">\n");
      out.write("                <a href=\"#\" data-target=\"{{maxPageNo}}\" data-url=\"{{lastUrl}}\" class=\"page-action\">尾页</a>\n");
      out.write("            </li>\n");
      out.write("        </ul>\n");
      out.write("    </div>\n");
      out.write("</div>\n");
      out.write("</script>\n");
      out.write("\n");
      out.write("<script type=\"text/javascript\">\n");
      out.write("    var paginateTemplate = $(\"#paginateTemplate\").html();\n");
      out.write("    Mustache.parse(paginateTemplate);\n");
      out.write("    \n");
      out.write("    function renderPage(url, total, pageNo, pageSize, currentSize, idElement, callback) {\n");
      out.write("        var maxPageNo = Math.ceil(total / pageSize);\n");
      out.write("        var paramStartChar = url.indexOf(\"?\") > 0 ? \"&\" : \"?\";\n");
      out.write("        var from = (pageNo - 1) * pageSize + 1;\n");
      out.write("        var view = {\n");
      out.write("            from: from > total ? total : from,\n");
      out.write("            to: (from + currentSize - 1) > total ? total : (from + currentSize - 1),\n");
      out.write("            total : total,\n");
      out.write("            pageNo : pageNo,\n");
      out.write("            maxPageNo : maxPageNo,\n");
      out.write("            nextPageNo: pageNo >= maxPageNo ? maxPageNo : (pageNo + 1),\n");
      out.write("            beforePageNo : pageNo == 1 ? 1 : (pageNo - 1),\n");
      out.write("            firstUrl : (pageNo == 1) ? '' : (url + paramStartChar + \"pageNo=1&pageSize=\" + pageSize),\n");
      out.write("            beforeUrl: (pageNo == 1) ? '' : (url + paramStartChar + \"pageNo=\" + (pageNo - 1) + \"&pageSize=\" + pageSize),\n");
      out.write("            nextUrl : (pageNo >= maxPageNo) ? '' : (url + paramStartChar + \"pageNo=\" + (pageNo + 1) + \"&pageSize=\" + pageSize),\n");
      out.write("            lastUrl : (pageNo >= maxPageNo) ? '' : (url + paramStartChar + \"pageNo=\" + maxPageNo + \"&pageSize=\" + pageSize)\n");
      out.write("        };\n");
      out.write("        $(\"#\" + idElement).html(Mustache.render(paginateTemplate, view));\n");
      out.write("\n");
      out.write("        $(\".page-action\").click(function(e) {\n");
      out.write("            e.preventDefault();\n");
      out.write("            $(\"#\" + idElement + \" .pageNo\").val($(this).attr(\"data-target\"));\n");
      out.write("            var targetUrl  = $(this).attr(\"data-url\");\n");
      out.write("            if(targetUrl != '') {\n");
      out.write("                $.ajax({\n");
      out.write("                    url : targetUrl,\n");
      out.write("                    success: function (result) {\n");
      out.write("                        if (callback) {\n");
      out.write("                            callback(result, url);\n");
      out.write("                        }\n");
      out.write("                    }\n");
      out.write("                })\n");
      out.write("            }\n");
      out.write("        })\n");
      out.write("    }\n");
      out.write("</script>\n");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
