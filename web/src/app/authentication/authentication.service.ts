import { Injectable } from '@angular/core';
import {LohAuthTokenName, LohAuthUserId, LohAuthUserName} from './AuthTokens';
import {pipe, Subject} from 'rxjs';
import {ActivatedRoute, ActivatedRouteSnapshot, Router} from '@angular/router';
import {Message, MessageService} from 'primeng/api';
import {debounceTime, tap, throttleTime} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  userName: string;
  userId: string;
  token: string;
  userNameChanged = new Subject<string>();
  onUserUnauthorized = new Subject<string>();
  lastRoute: string;
  get isLogged() {
    return this.token && this.userName;
  }
  constructor(
    private router: Router, private messageService: MessageService, private activatedRoute: ActivatedRoute
  ) {
    this.onUserUnauthorized
      .pipe(tap(() => {
        if ( this.router.routerState.snapshot.url !== '/home') {
          this.lastRoute = this.router.routerState.snapshot.url;
          this.cleanTokenAndUserName();
          this.router.navigateByUrl(`/home`);
        }
        }),
        throttleTime(10000))
      .subscribe((message: string) => {
      this.notifyUserAboutUnauthorizedAccess(message);
    });
  }

  public setToken(token: string) {
    localStorage.setItem(LohAuthTokenName, token);
    this.token = token;
  }
  public getToken() {
    return localStorage.getItem(LohAuthTokenName);
  }

  public publishNewUserName(userName: string, userId: string) {
    localStorage.setItem(LohAuthUserName, userName);
    localStorage.setItem(LohAuthUserId, userId);
    this.userNameChanged.next(userName);
    this.userName = userName;
    this.userId = userId;
  }
  public getUser() {
    const userName = localStorage.getItem(LohAuthUserName);
    if (userName) {
      this.userNameChanged.next(userName);
      this.userName = userName;
    }
    const token = localStorage.getItem(LohAuthTokenName);
    if (token) {
      this.token = token;
    }
    const userId = localStorage.getItem(LohAuthUserId);
    if (userId) {
      this.userId = userId;
    }
  }
  public cleanTokenAndUserName() {
    this.token = null;
    this.userName = null;
    this.userNameChanged.next(null);
    localStorage.removeItem(LohAuthTokenName);
    localStorage.removeItem(LohAuthUserName);
  }


  private  notifyUserAboutUnauthorizedAccess(message: string) {
    this.messageService.add(<Message>{
      severity: 'error',
      summary: 'Non Authorized',
      details: message
    });
  }
}
