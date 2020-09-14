## Miso Sangmyung Random Chat App
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
0. ~~Apache License 2.0 출처 명시~~
1. Main2Activity - 로그인 화면배치, 기능 변경
2. 회원정보 한글로 변경
3. 태그 목록 ScrollView로 구현하여 조작성 확보
4. 기능 테스트 버튼 정렬 (형찬이가 구현한 Alarm 기능 버튼 이름 부여)
5. 학과에 맞는 프로필 사진 추가
6. 65 Line, mappingDepartmentToProfileImage() 함수 구현
  - 학과 이름을 DB에서 가져오면 프로필 사진을 학과에 맞게 수정
  - 컴퓨터과학전공, 생명공학전공, 휴먼지능정보공학전공, 음악학부 일때만 적용.
  - 그 외 전공일 시 기본 프로필 사진 적용 (2020.07.26) 
    - ChatActivity의 messengerImageView(채팅 프로필사진)도 동일한 기능으로 구현 예정

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
        
- **2020/08/31** </br>
1. 과끼리 채팅 테스트
  - A user가 채팅방에 최초 진입 -> DB deptMap에 (학과 : A uid) 형태로 push, SplashActivity가 해당 roomid와 함께 출력 (Toast : roomid)
  - B user가 다음 채팅방에 진입 -> deptMap의 이미 대기해있던 같은 학과 A의 uid값이 pop
    - rooms - roomid - users - uid : department 값으로 저장 됨
- A는 SplashActivity에서 ChatActivity로 진입 -> B는 바로 ChatActivity로 진입 함. (Toast : 사용자 2 입장)
2. ChatActivity toolbar 사용, 우측 버튼으로 방 나가기, 상대방 신고 버튼 세팅.
3. 학번, 비밀번호 미입력 하고 탈퇴하기 버튼 눌렀을 때 앱 종료 이슈 수정
4. 자잘한 nullable 변수 변경

- **2020/09/03** </br>
1. ChatActitivy, SplashActivity 순서 정렬
2. 이메일 인증이 안 됐음에도 Auth는 들어가 기존회원이라 인식하는 문제 해결
3. 채팅 중 매칭 종료 기능 구현
  - ~~ISSUE#19 ChatActivity 매칭 종료 시 다른 roomid를 가진 사용자의 방과 randomRoomId가 초기화 되는 현상.~~
  - ~~ISSUE#20 ChatFragment 진입시 앱이 종료되는 현상( activity에서 fragment로 보내는 bundle때문에 일어나는 것으로 확인)~~
  - ~~ISSUE#14 SplashActivity Issue.~~
  
- **2020/09/07** </br>
1. 채팅 방 나가기 구현 완료
2. splashactivity- 뒤로가기 종료 시 서버에 있는 채팅 관련 정보 삭제, 개인 randomroomid 삭제
3. ISSUE #24 RecyclerView issue
    - 현재 ChatFragment에서 메시지를 보낼 때 본인 및 상대방의 recyclerview에 반영이 안되는 issue
      => recyclerview 생성에 관한 문제
4. FCM 푸시 알람 테스트

- **2020/09/10 ~ 11** </br>
1. 채팅 방에 입력한 메시지 띄우기 기능 구현
2. TagsFragment 기능 개발 시작
3. FCM 푸시 알람 기능 개발

- **2020/09/14**</br>
1. 채팅 방 메시지 띄우기 기능 구현 완료 (상대방 메시지, 자신의 메시지 구분짓기 해야함)
2. TagsFragment 기능 구현 중, 태그 추가, DB 저장, 조회, 삭제 기능
3. 채팅 방 입장 후 앱을 백그라운드에 놓고, 상대방이 매칭 되면 푸시 알람으로 상대방의 입장 여부 알림 구현
