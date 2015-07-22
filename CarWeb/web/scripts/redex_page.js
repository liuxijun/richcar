function rebuild_page_nav(nav, page_count, page_index, page_func,record_count){
    nav.html('');
    nav.append('<li class="disabled"><a>' + record_count + '条</a></li>');
    if (page_count > 1) {
        nav.append((page_index == 1) ? '<li class="disabled"><a href="#">首页</a></li>' :
                              '<li><a href="#" onclick="' + page_func + '(1);return false;">首页</a></li>');
        nav.append((page_index == 1) ? '<li class="disabled"><a href="#">上一页</a></li>' :
                              '<li><a href="#" onclick="' + page_func + '(' + (page_index - 1) + ');return false;">上一页</a></li>');
        var start_page = page_index - 1;
        var end_page = start_page + 5;
        if (end_page > page_count) {
            end_page = page_count;
            start_page = end_page - 4;
        }
        if (start_page < 1) {
            start_page = 1;
            end_page = (start_page + 4 > page_count) ? page_count : start_page + 4;
        }

        // display page index
        for (var i = start_page; i <= end_page; i++) {
            nav.append((i == page_index) ? '<li class="active"><a href="#">' + i + '</a></li>' :
                                  '<li><a href="#" onclick="' + page_func + '(' + i + ');return false;">' + i + '</a></li>');
        }

        nav.append((page_index == page_count) ? '<li class="disabled"><a href="#">下一页</a></li>' :
                              '<li><a href="#" onclick="'+ page_func + '(' + (page_index + 1) + ');return false;">下一页</a></li>');
        nav.append((page_index == page_count) ? '<li class="disabled"><a href="#">尾页</a></li>' :
                              '<li><a href="#" onclick="' + page_func + '(' + page_count + ');return false;">尾页</a></li>');
    }
}
