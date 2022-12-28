<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 작성</title>
</head>

<body>
 <div class="row">
        	<div class="col-md-12">
        		<div class="box box-primary">
					<div class="box-header with-border">
					<h3 class="box-title">게시글 작성</h3>
					</div>


					<form role="form" method="post" action="register">
						<div class="box-body">
							<div class="form-group">
							<label for="title">제목</label>
							<input type="text" class="form-control" id="title" name="title" placeholder="제목을 작성해주세요.">
							</div>
							<div class="form-group">
							<label for="writer">작성자</label>
							<input type="text" class="form-control" id="writer" name="writer"  placeholder="이름을 적어주세요.">
							</div>
							<div class="form-group">
							<label for="content">Content</label>
							<textarea class="form-control" id="content" name="content"></textarea>
							</div>
						</div>
						<div class="box-footer">
							<button type="submit" class="btn btn-primary">등록하기</button>
							<a href="/board/list"><button type="button" class="btn btn-primary">목록으로</button></a> 
						</div>
					</form>
				</div>
        	</div>
        </div>

</body>
</html>