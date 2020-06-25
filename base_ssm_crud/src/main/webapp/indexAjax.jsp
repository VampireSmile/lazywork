<%--
  Author: YinJiaWei
  Date: 2020/6/18
  Time: 19:40
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         pageEncoding="UTF-8" %>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <title>员工列表</title>
    <%
        //项目地址，比如本项目地址：http://localhost:8080/bscrud
        pageContext.setAttribute("APP_PATH", request.getContextPath());
    %>
    <!--引入jQuery-->
    <!--
    web路径：
        不以/开始的相对路径：找资源，以当前资源的路径为基准，经常容易出问题
        以/开始的相对路径：找资源，以服务器的路径为标准(http://localhost:80)
        比如：http://localhost:80/crud
    -->
    <script type="text/javascript" src="${APP_PATH}/staticSources/jquery1.11.0/jquery-1.11.0.min.js"></script>

    <!--通过cdn引入bootstrap-->
    <!-- 3.3.7版本的 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
          crossorigin="anonymous">
    <!-- 3.3.7的 Bootstrap 核心 JavaScript 文件 -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
    <!--也可以通过静态资源导入bootstrap-->
    <%--    <link href="${APP_PATH}/staticSources/bootstrap-4.4.1-dist/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>--%>
    <%--    <script type="text/javascript" src="${APP_PATH}/staticSources/bootstrap-4.4.1-dist/js/bootstrap.min.js"/>--%>

</head>
<body>

<!-- 添加修改员工Modal -->
<div class="modal fade" id="empAddModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel"></h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="inputEmpName" class="col-sm-2 control-label">员工姓名</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="empName" id="inputEmpName"
                                   placeholder="EmpName">
                            <span id="helpBlockName" class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputEmail" class="col-sm-2 control-label">员工邮箱</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="email" id="inputEmail"
                                   placeholder="Email@gmail.com">
                            <span id="helpBlockEmail" class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">员工性别</label>
                        <div class="col-sm-10">
                            <label class="radio-inline">
                                <input type="radio" name="gender" id="genderM" value="M" checked="checked"> 男
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="gender" id="genderF" value="F"> 女
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">员工部门</label>
                        <div class="col-sm-6">
                            <select class="form-control" name="dId" id="selectDeptName"></select>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="emp_add_save">保存</button>
            </div>
        </div>
    </div>
</div>

<!--单个删除员工modal-->
<div class="modal fade" id="delModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">员工删除</h4>
            </div>
            <div class="modal-body"><p></p></div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="confirm_del_btn">确认</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<!--批量删除员工modal-->
<div class="modal fade" id="delMoreModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">批量删除</h4>
            </div>
            <div class="modal-body"><p></p></div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="confirm_delMore_btn">确认</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<!--构建主页面-->
<div class="container">

    <!--标题-->
    <div class="row">
        <div class="col-md-12"><h1>BSCRUD</h1></div>
    </div>

    <!--新增和批量删除按钮-->
    <div class="row">
        <div class="col-md-2 col-md-offset-9">
            <button class="btn btn-primary glyphicon glyphicon-plus" id="add_emp_btn">新增</button>
            <button class="btn btn-danger glyphicon glyphicon-trash" id="del_more_btn">删除</button>
        </div>
    </div></br>

    <!--显示表格数据-->
    <div class="row">
        <div class="col-md-12">
            <table class="table table-hover" id="emps_table">
                <thead>
                <tr>
                    <th><input type="checkbox" id="select_all_emps"/></th>
                    <th>#</th>
                    <th>empName</th>
                    <th>gender</th>
                    <th>email</th>
                    <th>deptName</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
        </div>
    </div>

    <!--显示分页信息-->
    <div class="row">
        <!--分页文字信息-->
        <div class="col-md-6" id="page_info_area"></div>
        <!--分页条信息-->
        <div class="col-md-6" id="page_nav_area"></div>
    </div>

</div>

<script type="text/javascript">

    //页面加载完成之后，直接发送ajax请求，获得分页数据
    $(function () {
        to_page(1);//默认第一页
    });

    //跳转页面
    function to_page(pn) {
        $.ajax({
            url: "${APP_PATH}/emps",
            data: {"pn": pn},
            type: "GET",
            success: function (result) {
                //1.解析并显示员工数据
                build_emps_table(result);
                //2.解析并显示分页信息
                build_page_info(result);
                //3. 解析并显示分页条信息
                build_page_nav(result);
            }
        });
    }

    //构建表格
    function build_emps_table(result) {
        $("#emps_table tbody").empty();
        $("#select_all_emps").removeAttr("checked");
        let emps = result.extend.pageInfo.list;
        $.each(emps, function (index, item) {
            let empCheckBox = $("<td></td>").append($("<input>").attr("type", "checkbox").addClass("check_item"));
            let empIdTd = $("<td></td>").append(item.empId);
            let empNameTd = $("<td></td>").append(item.empName);
            let empGenderTd = $("<td></td>").append(item.gender == 'M' ? '男' : '女');
            let empEmailTd = $("<td></td>").append(item.email);
            let empDNameTd = $("<td></td>").append(item.dName);
            let editBtn = $("<button></button>").addClass("btn btn-primary btn-sm edit_button")
                .append($("<span></span>").addClass("glyphicon glyphicon-pencil").attr("aria-hidden", "true"))
                .append("编辑").attr("editId", item.empId);
            let delBtn = $("<button></button>").addClass("btn btn-danger btn-sm delete_button")
                .append($("<span></span>").addClass("glyphicon glyphicon-trash").attr("aria-hidden", "true"))
                .append("删除").attr("delId", item.empId);
            let opBtn = $("<td></td>").append(editBtn, " ", delBtn);
            $("<tr></tr>").append(empCheckBox, empIdTd, empNameTd, empGenderTd, empEmailTd, empDNameTd, opBtn)
                .appendTo("#emps_table tbody");
        });
    }

    //构建分页信息
    function build_page_info(result) {
        $("#page_info_area").empty();
        $("#page_info_area").append("当前第", $("<span></span>").addClass("label label-default").append(result.extend.pageInfo.pageNum), "页 | ",
            "总", $("<span></span>").addClass("label label-default").append(result.extend.pageInfo.pages), "页 | ",
            "总", $("<span></span>").addClass("label label-default").append(result.extend.pageInfo.total), "条记录");
    }

    //构建分页条信息
    function build_page_nav(result) {
        $("#page_nav_area").empty();
        let navigationData = result.extend.pageInfo.navigatepageNums;
        let pageNum = result.extend.pageInfo.pageNum;
        let sumPages = result.extend.pageInfo.pages;
        let hasPrePage = result.extend.pageInfo.hasPreviousPage;
        let hasNextPage = result.extend.pageInfo.hasNextPage;
        let navPages = $("<nav></nav>").attr("aria-label", "Page navigation");
        let ulPages = $("<ul></ul>").addClass("pagination");
        let firstPage = $("<li></li>").append($("<a></a>").attr("href", "#").append("首页"));
        let previousPage = $("<li></li>").append($("<a></a>").attr({
            "href": "#",
            "aria-label": "Previous"
        })
            .append($("<span></span>").attr("aria-hidden", "true").append("&laquo;")));
        if (hasPrePage == false) {
            previousPage.addClass("disabled");
            firstPage.addClass("disabled");
        } else {
            firstPage.click(function () {
                to_page(1);
            });
            previousPage.click(function () {
                to_page(pageNum - 1);
            });
        }
        ulPages.append(firstPage, previousPage);
        $.each(navigationData, function (index, item) {
            let tmp = $("<li></li>").append($("<a></a>").attr("href", "#").append(item));
            if (item == pageNum)
                tmp.addClass("active");
            tmp.click(function () {
                to_page(item);
            });
            ulPages.append(tmp);
        });
        let nextPage = $("<li></li>").append($("<a></a>").attr({
            "href": "#",
            "aria-label": "Next"
        })
            .append($("<span></span>").attr("aria-hidden", "true").append("&raquo;")));
        let lastPage = $("<li></li>").append($("<a></a>").attr("href", "#").append("尾页"));
        if (hasNextPage == false) {
            nextPage.addClass("disabled");
            lastPage.addClass("disabled");
        } else {
            nextPage.click(function () {
                to_page(pageNum + 1);
            });
            lastPage.click(function () {
                to_page(sumPages);
            });
        }
        ulPages.append(nextPage, lastPage);
        navPages.append(ulPages);
        $("#page_nav_area").append(navPages);
    }

    //清除modal状态
    function clearAddPanelStatus() {
        $("#inputEmpName").parent().removeClass("has-error has-success");
        $("#inputEmpName").next("span").text("");
        $("#inputEmail").parent().removeClass("has-error has-success");
        $("#inputEmail").next("span").text("");
        $("#selectDeptName").empty();
        $("#emp_add_save").removeAttr("ajax-Valid1");
        $("#emp_add_save").removeAttr("ajax-Valid2");
        $("#emp_add_save").removeAttr("operater");
        $("#emp_add_save").removeAttr("update_emp_id");
    }

    //查出所有部门信息函数
    function getDepts() {
        $.get('${APP_PATH}/depts', function (result) {
                //{"code":200,"msg":"处理成功！",
                // "extend":{"depts":
                // [{"deptId":1,"deptName":"开发部"},{"deptId":2,"deptName":"测试部"}]}}
                $.each(result.extend.depts, function (index, item) {
                    $("<option></option>").attr("value", item.deptId)
                        .append(item.deptName).appendTo($("#empAddModal select"));
                });
            }
        );
    }

    //查出员工及部门信息函数
    function getEmpInfoWithDept(empid) {
        clearAddPanelStatus();
        $("#empAddModal form")[0].reset();
        $("#myModalLabel").text("员工修改");
        $("#emp_add_save").attr({
            "operater": "edit",
            "update_emp_id": empid
        });
        getDepts();
        $.get("${APP_PATH}/emp/" + empid, function (result) {
            let empInfo = result.extend.empInfo;
            $("#inputEmpName").val(empInfo.empName);
            $("#inputEmail").val(empInfo.email);
            $("#empAddModal input[name = gender]").val([empInfo.gender]);
            $("#selectDeptName").val([empInfo.dId]);
        });
    }

    //更新信息函数
    function updateEmpInfo() {
        $.ajax({
            url: "${APP_PATH}/emp/" + $("#emp_add_save").attr("update_emp_id"),
            data: $("#empAddModal form").serialize(),
            type: "PUT",
            success: function (result) {
                //1.关闭对话框
                //2.回到本页面
                if (result.code == 200) {
                    $("#empAddModal").modal("hide");
                    to_page($("#page_info_area span:first-child").text());
                }
            }
        });
    }

    //保存员工信息函数
    function saveEmpInfo() {
        //1.前端校验数据
        if (!validate_form() ||
            $("#emp_add_save").attr("ajax-Valid1") == "false" ||
            $("#emp_add_save").attr("ajax-Valid2") == "false") return;
        //2.发送ajax请求，提交表单
        let data = $("#empAddModal form").serialize();
        //3.后端校验数据
        $.post("${APP_PATH}/emp", data, function (result) {
            if (result.code == 200) {
                to_page(1);
                $("#empAddModal").modal("hide");
            } else {
                if (undefined != result.extend.errorFields.empName)
                    show_valid_info("#inputEmpName", "error", result.extend.errorFields.empName);
                if (undefined != result.extend.errorFields.email)
                    show_valid_info("#inputEmail", "error", result.extend.errorFields.email);
            }
        });
    }

    //前端校验表单数据格式
    function validate_form() {
        //1、使用正则表达式
        let form_input_name = $("#inputEmpName").val();
        let form_input_email = $("#inputEmail").val();
        let regName = /(^[a-zA-Z0-9_-]{3,12}$)|(^[\d\u2E80-\u9FFF]{2,6}$)/;
        let regEmail = /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/;
        if (!regName.test(form_input_name)) {
            show_valid_info("#inputEmpName", "error", "只接受2-6位的中文和数字或3-12位的英文和数字");
            return false;
        }
        if (!regEmail.test(form_input_email)) {
            show_valid_info("#inputEmail", "error", "邮箱格式错误");
            return false;
        }
        return true;
    }

    /**
     * 展示数据状态信息
     * @param ele
     * @param status
     * @param msg
     */
    function show_valid_info(ele, status, msg) {
        $(ele).parent().removeClass("has-error has-success");
        $(ele).next("span").text("");
        if (status == "success")
            $(ele).parent().addClass("has-success");
        else if (status == "error")
            $(ele).parent().addClass("has-error");
        $(ele).next("span").text(msg);
    }

    //需要给每一个编辑按钮添加click事件
    //两种方法：1）.创建的时候添加click事件;2）.使用.live()可以为我们后来的元素添加事件
    //但在jQuery高版本已被on方法取代
    $(document).on("click", ".edit_button", function () {
        //获取员工数据并显示在modal中
        getEmpInfoWithDept($(this).attr("editId"));
        //弹出模态框
        $("#empAddModal").modal({
            backdrop: "static"
        });
    });

    //需要给每一个删除按钮添加click事件
    $(document).on("click", ".delete_button", function () {
        $("#confirm_del_btn").attr("save_confirm_del", $(this).attr("delId"));
        let tmp = $("<span></span>").css({
            "font-weight": "bolder",
            "color": "\#FF0000"
        }).append($(this).parents("tr").find("td:eq(2)").text());
        $("#delModal p").empty();
        $("#delModal p").append("确认删除 ", tmp, " 吗?");
        //弹出模态框
        $("#delModal").modal({
            backdrop: "static"
        });
    });

    //select_all_emps点击将当前页面所有checkBox全选
    $("#select_all_emps").click(function () {
        //attr获取checked的值为undefine；
        //dom原生属性使用prop方法来修改和获取属性值，attr获取自定义属性的值
        $(".check_item").prop("checked", $(this).prop("checked"));
    });

    //当前页面所有CheckBox全选后将select_all_emps选中
    $(document).on("click", ".check_item", function () {
        //判断当前选中的check_item是不是当前页面记录总数
        let flag = $(".check_item:checked").length == $(".check_item").length;
        //设置select_all_emps属性
        $("#select_all_emps").prop("checked", flag);
    });

    //发送ajax请求校验用户名是否可用
    $("#inputEmpName").change(function () {
        let nameVal = this.value;
        $.post("${APP_PATH}/check", {"data": nameVal, "userOrEmail": "user"}, function (result) {
            if (result.extend.valid_msg_code == 100) {
                show_valid_info("#inputEmpName", "success", result.extend.valid_msg_body);
                $("#emp_add_save").attr("ajax-Valid1", "true");
            } else {
                show_valid_info("#inputEmpName", "error", result.extend.valid_msg_body);
                $("#emp_add_save").attr("ajax-Valid1", "false");
            }
        });
    });

    //发送ajax请求校验email是否可用
    $("#inputEmail").change(function () {
        let emailVal = this.value;
        $.post("${APP_PATH}/check", {"data": emailVal, "userOrEmail": "email"}, function (result) {
            if (result.extend.valid_msg_code == 200) {
                show_valid_info("#inputEmail", "success", result.extend.valid_msg_body);
                $("#emp_add_save").attr("ajax-Valid2", "true");
            } else {
                show_valid_info("#inputEmail", "error", result.extend.valid_msg_body);
                $("#emp_add_save").attr("ajax-Valid2", "false");
            }
        });
    });

    //新增按钮点击事件
    $("#add_emp_btn").click(function () {
        clearAddPanelStatus();
        $("#empAddModal form")[0].reset();
        $("#emp_add_save").attr("operater", "save");
        //需要先发送ajax请求获得部门信息显示在下拉列表中
        getDepts();
        //设置title
        $("#myModalLabel").text("员工添加");
        //弹出模态框
        $("#empAddModal").modal({
            backdrop: "static"
        });
    });

    //保存员工新增信息或者修改数据
    $("#emp_add_save").click(function () {
        if ($(this).attr("operater") == "save") {
            saveEmpInfo();
        } else {
            updateEmpInfo();
        }
    });

    //删除员工
    $("#confirm_del_btn").click(function () {
        $.ajax({
            url: "${APP_PATH}/emp/" + $(this).attr("save_confirm_del"),
            type: "DELETE",
            success: function (result) {
                console.log(result);
                if (result.code == 200) {
                    $("#delModal").modal('hide');
                    to_page($("#page_info_area span:first-child").text());
                } else alert("删除失败");
            }
        });
    });

    //批量删除modal
    $("#del_more_btn").click(function () {
        let hasCheck = $(".check_item:checked");
        $("#delMoreModal p").empty();
        if (hasCheck.length == 0) {
            $("#delMoreModal p").append("请先选择需要删除的员工...");
            //弹出模态框
            $("#delMoreModal").modal({
                backdrop: "static"
            });
            return;//结束
        }
        $("#delMoreModal p").append("确定删除 ");
        $.each(hasCheck, function (index, item) {
            let tmp = $("<span></span>").css({
                "font-weight": "bolder",
                "color": "\#FF0000"
            }).append($(item).parents("tr").find("td:eq(2)").text());
            $("#delMoreModal p").append(tmp, " ");
        });
        $("#delMoreModal p").append(" 吗?");
        //弹出模态框
        $("#delMoreModal").modal({
            backdrop: "static"
        });
    });

    //确认批量删除
    $("#confirm_delMore_btn").click(function () {
        let checker = $(".check_item:checked");//找到所有选中的员工记录
        if (checker.length == 0) return;//如果没有选中任何员工肯定不能发送求
        let parmData = "";//批量删除的id
        $.each(checker, function (index, item) {
            parmData += $(item).parents("tr").find("td:eq(1)").text();
            parmData += "-";
        });
        parmData = parmData.substr(0, parmData.length - 1);
        $.ajax({
            url: "${APP_PATH}/emp/" + parmData,
            type: "DELETE",
            success: function (result) {
                if (result.code == 200) {
                    $("#delMoreModal").modal('hide');
                    to_page($("#page_info_area span:first-child").text());
                } else alert("删除失败");
            }
        });
    });

</script>
</body>
</html>
