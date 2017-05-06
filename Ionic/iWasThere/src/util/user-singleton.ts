export class UserSingleton {
    private static user: any = null;

    constructor(){
    }

    setInstance(name: string, nusp: string, teacher: boolean) {
        if (UserSingleton.user == null) {
            UserSingleton.user = {name: name, nusp: nusp, teacher: teacher};
        }
        return UserSingleton;
    }

    getInstance() {
        return UserSingleton.user;
    }

    deleteInstance() {
        UserSingleton.user = null;
    }

}