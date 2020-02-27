# Android-Architecture-Components.

Android Architecture Components를 이해하기 위해 스스로 공부한 것을 정리하고 직접 프로젝트로 만들어보며 이해하기 위한 레퍼지토리입니다.

- [모바일 앱 사용자 환경](https://github.com/Jaesungchi/Android-Architecture-Components#모바일-앱-사용자-환경)
- [일반 아키텍처 원칙](https://github.com/Jaesungchi/Android-Architecture-Components#일반-아키텍처-원칙)
- [권장 앱 아키텍처](https://github.com/Jaesungchi/Android-Architecture-Components#권장-앱-아키텍처)

## 모바일 앱 사용자 환경

데스크톱 앱과 안드로이드의 다른점은 데스크톱 앱의 경우는 단일 진입점이 있고 하나의 모놀리식 프로세스로 실행됩니다. 하지만 안드로이드는 액티비티, 프래그먼트, 서비스, Content Provider, Broadcast Receiver등의 여러 앱 구성요소가 포함됩니다.

안드로이드에서는 짧은 시간내에 여러 앱과 상호작용할때가 많아 앱이 사용자 중심의 다양한 워크플로 와 작업에 맞게 조정이 되어야합니다. 왜냐하면 다양한 과정에서 다른 앱을 들어갔다가 돌아온다던지, 화면이 회전된다던지 , 전화가 온다던지 등의 다양한 과정에서 사용환경이 중단 될 수 있습니다. 이러한 환경에서 중단이 된 후 다시 돌아가도 하던 작업들이 남아있어야 합니다. 또한, 운영체제에서 새로운 앱을 위한 공간을 확보하기 위해 언제든지 앱의 프로세스를 종료할 수 있습니다.

이러한 환경을 고려해보면 운영체제나 사용자가 앱의 구성요소를 언제든지 제거 할 수 있기 때문에 앱 구성요소에 앱 데이터나 상태를 저장해서는 안되며 앱 구성요소가 서로 종속되면 안됩니다.

## 일반 아키텍처 원칙

앱 데이터와 상태를 저장하는데 Component를 사용할 수없다면 어떻게 설계해야할까?

- **관심사 분리**

  가장 중요한 원칙으로 UI기반 클래스인 Activity와 Fragment에는 UI 및 운영체제 상호작용을 처리하는 로직만 포함 시킵니다. 이러한 클래스는 최대한 가볍게 유지해, 많은 생명 주기 관련 문제를 피합니다.

- **모델에서 UI 만들기**

  가급적 지속적인 모델에서 UI를 만들어야 합니다. 모델은 앱의 데이터 처리를 담당하는 구성요소로, 앱의 View 객체 및 앱 구성요소와 독립되어 있어 앱의 수명 주기 및 관려 문제의 영향을 받지 않습니다. 지속 모델이 이상적인 이유는 운영체제에서 Resource확보를 위해 앱을 제거해도 사용자 데이터가 삭제되지 않고, 네트워크 연결이 취약하거나 연결되지 않아도 앱이 계속 작동하기때문입니다.

## 권장 앱 아키텍처

![img](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)

각 구성요소가 한 수준 아래의 구성요소에만 종속됩니다. 이것이 제일 중요합니다.

이 설계는 사용자가 앱을 마지막으로 닫은 시점과 상관없이 앱이 로컬에 보존하는 사용자의 정보가 표시됩니다.

- **사용자 인터페이스 제작**

  예시 프로젝트의 UI는 Fragment와 xml 파일로 제작 됩니다.(UserProfileFragment,user_profile_layout.xml)

  UI를 만들기 위해선 데이터모델에 다음과 같은 요소가 있습니다.

  - 사용자 ID : 사용자의 식별자로, 프래그먼트 인수를 이용하여 이 정보를 프래그먼트에 전달합니다. 운영체제에서 프로세스를 제거해도 이 정보가 유지되므로, 앱을 다시 시작할때 ID를 사용할 수 있습니다.
  - 사용자 객체 : 사용자에 관한 세부 정보를 보유하는 데이터 클래스입니다.

  ViewModel 아키텍처 구성요소에 기반한 ViewModel을 사용하여 이 정보를 유지합니다.

  > **ViewModel은 프래그먼트나 Activity같은 특정 UI 구성요소에 관한 데이터를 제공하고 모델과 커뮤니케이션하기 위한 데이터 처리 비즈니스 로직을 포함하고 있습니다. 따라서 ViewModel은 UI 구성요소에 관해 알지 못하므로 구성 변경의 영향을 받지 않는다.**

  - user_profile_layout.xml : 화면의 UI 레이아웃 정의
  - UserProfileFragment : 데이터를 표시하는 UI컨트롤러
  - UserProfileViewModel : Fragment에서 볼 수 있도록 데이터를 준비하고 사용자 상호작용에 반응하는 클래스.

  ```kotlin
  class UserProfileViewModel : ViewModel(){
  	val userId : String = TODO()
  	val user : User = TODO()
  }
  ```

  ```kotlin
  class UserProfileFragment : Fragment(){
      private val viewModel : UserProfileViewModel by viewModels()
      override fun onCreateView(
          inflter : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View{
          return inflter.inflate(R.layout.main_fragment,container,false)
      }
  }
  ```

  이러한 코드 모듈로 구성되는데 이 모듈을 연결하기 위해서는 UserProfileViewModel 클래스에서 user 필드가 생성되면 UI에게 알려주는 방법이 필요합니다.

  user를 가져오려면 ViewModel에서 프래그먼트 인수에 액세스 해야합니다. 프래그먼트에서 인수를 전달할 수 있고, 더 나은 방법으로는 **SavedState모듈을 사용해 ViewModel에서 직접 인수를 읽도록 할 수 있습니다.**

  ```kotlin
  // UserProfileViewModel
      class UserProfileViewModel(
         savedStateHandle: SavedStateHandle
      ) : ViewModel() {
         val userId : String = savedStateHandle["uid"] ?:
                throw IllegalArgumentException("missing user id")
         val user : User = TODO()
      }
  
      // UserProfileFragment
      private val viewModel: UserProfileViewModel by viewModels(
         factoryProducer = { SavedStateViewModelFactory(getApplication(),this) }
         ...
      )
      
  ```

  이제 사용자 객체가 확보되면 프래그먼트에 알려야합니다. 이때에는 LiveData 가 사용됩니다.

  > **LiveData**는 식별 가능한 데이터 홀더 입니다. 앱의 다른 구성요소에서는 이 홀더를 사용해 상호 간 명시적이고 엄격한 종속성 경로를 만들지 않고도 객체 변경사항을 모니터링 할 수 있습니다. 또한 LiveData 구성요소는 Activity, Fragment, Service와 같은 앱 구성요소의 수명 주기를 고려하고 객체 유출과 과한 메모리 소비를 방지하기 위한 정리 로직을 포함하기 때문입니다.

  LiveData 구성요소를 앱에 통합하기 위해 UserProfileViewModel의 필드유형을 LiveData<User>로 변경합니다. 이제 데이터가 업데이트 되면 UserProfileFragment에 정보가 전달됩니다. 또한 LiveData필드는 수명 주기를 인식하기 때문에 더 이상 필요하지 않은 참조를 자동으로 정리합니다.
  
  UserProfileViewModel
  
  ```kotlin
  class UserProfileViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
         val userId : String = savedStateHandle["uid"] ?:
                throw IllegalArgumentException("missing user id")
         val user : LiveData<User> = TODO()
      }
  ```
  
  UserProfileFragment
  
  ```kotlin
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      viewModel.user.observe(viewLifecycleOwner){
          //Update UI
      }
  }
  ```
  
  이제 사용자 프로필 데이터가 업데이트 될때맏 onChanged() 콜백이 호출되고 UI가 새로고침 됩니다.
  
  여기서 onStop()을 재정의 하지 않은 것은, LiveData는 수명 주기를 인식하기 때문에 LiveData의 경우 이 단계가 필요 없습니다.
  
  

---

# 출처

[앱 아키텍처 가이드](https://developer.android.com/jetpack/docs/guide)