<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 목록</title>
<script>
let msg = '${msg}';
if(msg != '') {
  alert(msg);
}
</script>
</head>

<body>
<div class="row">
  <div class="col-md-12">
    <div class="box box-primary">
      <div class="box-header with-border">
      <h3 class="box-title">게시판</h3>
      </div>

      <div class="box">
        <div class="box-body">
          <table class="table table-bordered">
            <tbody>
              
              <tr>
              <th style="width: 10%">번호</th>
              <th style="width: 30%">제목</th>
              <th style="width: 20%">작성자</th>
              <th style="width: 20%">작성일</th>
              <th style="width: 20%">조회수</th>
              </tr>
              
              <c:forEach items="${list }" var="board">
              <tr>
              <td>${board.bno }</td>
              <td>${board.title }</td>
              <td>${board.writer }</td>
              <td><fmt:formatDate value="${board.regdate }" pattern="yyyy-MM-dd hh:mm:ss"/></td>
              <td>${board.readnum }</td>
              </tr>
              </c:forEach>
            </tbody>
          </table>
          <a href="/board/register"><button type="button" class="btn btn-primary">글쓰기</button></a>
        </div>
      
      </div>
    </div>
  </div>
</div>
</body>

</html>