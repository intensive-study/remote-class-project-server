## 프로젝트 진행 규칙

### 브랜치명
**브랜치명은 git flow 전략에 의존합니다.**
- 기능 구현: feature/*
```shell
  git flow feature start ${feature_branch}
```
- 코드 스타일 수정: style/*
```shell
  git checkout -b stylefix/${stylefix_branch}
```
**style 브랜치는 권장 사항입니다!**  

- 버그 픽스 on develop branch: bugfix/*
```shell
  git checkout -b bugfix/${bugfix_branch}
```

- 버그 픽스 on main branch: hotfix/*
```shell
  git flow hotfix start ${hotfix_branch}
```
<br/>

### 커밋 메세지
**기능 커밋**  
`feat: ` 으로 시작, 이후 커밋 메세지를 정의합니다.  

**ex) feat: initialize project**  

**기능 커밋**  
`style: ` 으로 시작, 이후 커밋 메세지를 정의합니다.

**ex) style: fix-code-style**  

**버그 픽스 커밋**  
`[bug|hot]fix: ` 으로 시작, 이후 커밋 메세지를 정의합니다.  

**ex) bugfix: bug fix**

<br/>

### 충돌 방지 요청 사항
충돌이 발생하더라도 수정하면 됩니다만, 충돌이 발생할 가능성을 최대한 줄이도록 합시다 :)  
***작업 전 항상 pull 받고, 최신 상태를 유지해주세요 :)***
