## 프로젝트 진행 규칙

### 브랜치명
**브랜치명은 git flow 전략에 의존합니다.**  
**style 브랜치**는 권장 사항입니다!  

- 기능 구현: feature/*
```shell
  git flow feature start ${feature_branch} ${기반 브랜치명}
```
- 코드 스타일 수정: style/*
```shell
  git checkout -b style/${style_branch}
```

- 버그 픽스 on develop branch: bugfix/*
```shell
  git checkout -b bugfix/${bugfix_branch}
```

- 버그 픽스 on main branch: hotfix/*
```shell
  git flow hotfix start ${hotfix_branch}
```
- 리팩토링: refactor/*
```shell
  git checkout -b refactor/${refactor_branch}
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

#### ✔️ Pull Request 자동 생성 방법(postfix - `pr`)

아래 커밋 메세지 내용과 같이 `pull request`를 생성하고자 하는 커밋 메세지 뒤에 `pr` 를 붙입니다.  
원격 저장소로 푸시한 모든 브랜치와 커밋내용에 대해 `pull request`를 생성하는 것은 비효율적일 수 있으므로,  
`postfix`로 풀리케를 생성할 조건을 추가합니다.

```
feat: initialize project pr
```
**단, 생성한 브랜치명은 [feature/\*\, bugfix/\*\, hotfix/\*\, refactor/\*\] 의 포멧에 부합해야 `pull request`가 생성됩니다.**  
**style 브랜치에 경우, pull request까지 생성하지 않습니다.**  
[관련 페이지](https://github.com/zmfl1230/pr-create-automation)

<br/>

### 충돌 방지 요청 사항
충돌이 발생하더라도 수정하면 됩니다만, 충돌이 발생할 가능성을 최대한 줄이도록 합시다 :)  
***작업 전 항상 pull 받고, 최신 상태를 유지해주세요 :)***
