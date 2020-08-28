## Miso Sangmyung Chat App
### 상명대학교 컴퓨터과학과 Capstone Design 프로젝트입니다.
- **2020/04/18** </br>
firebase 플랫폼의 real-time database 종속항목을 추가해놓은 상태입니다.
- **2020/05/22**</br>
BottomNavigationBar(메인 화면) 구현
- **2020/05/25** </br>
Firebase로 회원 가입(추가), 삭제, 로그아웃</br>회원 정보 : FirebaseDatabase (uid, email, emailVerified, department) 회원 가입 시 추가
- **2020/07/21** </br>
패키지 명 통일
Android Firebase Codelab Grow Friendly Chat
- **2020/07/23** </br>
채팅 기능 프로젝트에 Migration
Fragment 간 액티비티 전환 구현</br>
채팅 시 자신 uid 값 Firebase에 저장 (추후 상대방 uid와 비교)</br>
Notification 기능 구현
- **2020/07/26** </br>
0. Apache License 2.0 출처 명시
1. Main2Activity - 로그인 화면배치, 기능 변경
2. 회원정보 한글로 변경
3. 태그 목록 ScrollView로 구현하여 조작성 확보
4. 기능 테스트 버튼 정렬 (형찬이가 구현한 Alarm 기능 버튼 이름 부여)
5. 학과에 맞는 프로필 사진 추가
6. 65 Line, mappingDepartmentToProfileImage() 함수 구현
- 학과 이름을 DB에서 가져오면 프로필 사진을 학과에 맞게 수정
- 컴퓨터과학전공, 생명공학전공, 휴먼지능정보공학전공, 음악학부 일때만 적용.
- 그 외 전공일 시 기본 프로필 사진 적용 (2020.07.26) 
  + ChatActivity의 messengerImageView(채팅 프로필사진)도 동일한 기능으로 구현 예정

- **2020/08/26 ~ 08/28** </br>
1. 학과 별 매칭 Database 구조화 (1:1 채팅방 만들기)
 - '과'끼리 버튼을 눌렀을 때
  - 처음 대기하는 사람이라면 deptMap-학과명-uid로 우선 저장
  - 만약 누가 대기하고 있다면 자신의 uid와 대기하는 사람의 uid를 Map에 담아 room 생성 후 채팅
  - 학과명 내의 uid값은 null값으로 초기화하여 비운다.
2. 전체 학과 별 매칭 Database 구조화 (1:1 채팅방 만들기)
  - 처음 대기하는 사람이라면 deptMap-전체학과-uid로 우선 저장
  - 만약 누가 대기하고 있다면 자신의 uid와 대기하는 사람의 uid를 Map에 담아 room 생성 후 채팅
  - 전체 학과 내의 uid값도 null값으로 초기화
3. ChatActivity 내에 ChatFragment 적용

- **2020/08/29** </br>
1. RandomChatFragment
    - 아무도 없이 먼저 매칭을 눌렀을 때 채팅 방으로 안들어가지는 버그 수정
2. SettingFragment
    - 사용자 정보 및 설정 페이지 구현 (기존 UI 변경, 대표색 배경)
    - 사용자 로그아웃 기능 구현
    - 사용자 삭제 기능 구현 
        - 학번, 비밀번호 입력하는 재인증 과정 후 계정 삭제
        - Firebase, Authentication, Databse 전부 데이터 삭제 확인 완료
