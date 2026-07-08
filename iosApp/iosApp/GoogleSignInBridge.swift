import UIKit
import GoogleSignIn
import Shared

class GoogleSignInBridge: NSObject, GoogleSignInHelper {

    func signIn(callback: any GoogleSignInCallback) {
        
        print("GoogleSignInBridge -> signIn() chamado")
        
        guard let rootViewController = findTopViewController() else {
            callback.onFailure(errorMessage: "Não foi possivel obter o UiViewController")
            return
        }

        GIDSignIn.sharedInstance.signIn(withPresenting: rootViewController) { result, error in
            if let error = error {
                let nsError = error as NSError
                if nsError.code == -5 {
                    print("GoogleSignInBridge -> Usuario cancelou o login")
                    callback.onCancel()
                } else {
                    callback.onFailure(errorMessage: error.localizedDescription)
                }
                return
            }

            guard let idToken = result?.user.idToken?.tokenString else {
                callback.onFailure(errorMessage: "IdToken não encontrado")
                return
            }

            let accessToken = result?.user.accessToken.tokenString ?? ""
            print("GoogleSignInBridge -> Sucesso! idToken obtido")
            callback.onSuccess(idToken: idToken, accessToken: accessToken)
        }
    }

    private func findTopViewController() -> UIViewController? {
        guard
            let windowScene = UIApplication.shared.connectedScenes
                .first(where: { $0.activationState == .foregroundActive }) as? UIWindowScene,
            let rootVC = windowScene.windows.first(where: { $0.isKeyWindow })?.rootViewController
        else { return nil }

        return topMost(of: rootVC)
    }

    private func topMost(of viewController: UIViewController) -> UIViewController {
        if let presented = viewController.presentedViewController {
            return topMost(of: presented)
        }
        if let nav = viewController as? UINavigationController,
            let visible = nav.visibleViewController
        {
            return topMost(of: visible)
        }
        if let tab = viewController as? UITabBarController,
            let selected = tab.selectedViewController
        {
            return topMost(of: selected)
        }
        return viewController
    }
}
