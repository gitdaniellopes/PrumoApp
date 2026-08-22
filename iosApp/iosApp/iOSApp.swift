import SwiftUI
import GoogleSignIn
import FirebaseCore
import Firebase
import Shared

class AppDelegate: NSObject, UIApplicationDelegate {
  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
      
      #if DEBUG
      AppLogger.shared.initializedDebug()
      #endif
      
      FirebaseApp.configure()
      
      if let clientId = FirebaseApp.app()?.options.clientID {
          GIDSignIn.sharedInstance.configuration = GIDConfiguration(clientID: clientId)
      }

    return true
  }
    
    func application(
        _ app: UIApplication,
        open url: URL,
        options: [UIApplication.OpenURLOptionsKey : Any] = [:]
    ) -> Bool{
        return GIDSignIn.sharedInstance.handle(url)
    }
}

@main
struct iOSApp: App {
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
