import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor, HttpErrorResponse
} from '@angular/common/http';
import {Observable, of, throwError} from 'rxjs';
import {LohAuthTokenName} from '../authentication/AuthTokens';
import {AuthenticationService} from '../authentication/authentication.service';
import {Router} from '@angular/router';
import {catchError} from 'rxjs/operators';
import {Message, MessageService} from 'primeng/api';

@Injectable()
export class AuthenticationInterceptor implements HttpInterceptor {

  constructor(private readonly service: AuthenticationService, private router: Router, private messageService: MessageService
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = this.service.getToken();
    if (token) {
      const authReq = request.clone({
        headers: request.headers.set('Authorization', token)
      });
      return next.handle(authReq).pipe(catchError(x => this.handleAuthError(x)));
    } else {
      return next.handle(request).pipe(catchError(x => this.handleAuthError(x)));
    }
  }
  private handleAuthError(err: HttpErrorResponse): Observable<any> {
    if (err.status === 401 || err.status === 403) {
      this.messageService.add(<Message>{
        severity: 'error',
        summary: 'Non Authorized',
        details: err.message
      });
      this.router.navigateByUrl(`/home`);
      // if you've caught / handled the error, you don't want to rethrow it unless you also want downstream consumers to have to handle it as well.
      return of(err.message); // or EMPTY may be appropriate here
    }
    return throwError(err);
  }
}
