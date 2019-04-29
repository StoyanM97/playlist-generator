import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';


@Injectable({ providedIn: 'root' })
export class SearchService {

word:string;
searchSubject: BehaviorSubject<any>;
searchWord: Observable<any>;

constructor(){
    this.searchSubject = new BehaviorSubject<any>(this.word);
    this.searchWord = this.searchSubject.asObservable();
}


public get getSearchWord(): string {
    return this.searchSubject.value;
}

setSearchWord(word: string){
    this.searchSubject.next(word);
}

}