## Miso Sangmyung University Official App
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

- **2020/08/28** </br>
1. 학과 별 매칭 Database 구조화 (1:1 채팅방 만들기)
2. 전체 학과 별 매칭 Database 구조화 (1:1 채팅방 만들기)
