import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Filter } from '../models/Filter';


@Injectable({ providedIn: 'root' })
export class SearchService {

refreshStatus:string;
refreshStatusSubject: BehaviorSubject<any>;
refreshStatusObservable: Observable<any>; 

word:string;
searchSubject: BehaviorSubject<any>;
searchWord: Observable<any>;

filter: Filter;
filterSubject: BehaviorSubject<any>;
filterObject: Observable<any>;

constructor(){
    this.searchSubject = new BehaviorSubject<any>(this.word);
    this.searchWord = this.searchSubject.asObservable();

    this.filterSubject = new BehaviorSubject<any>(this.filter);
    this.filterObject = this.filterSubject.asObservable();

    this.refreshStatusSubject = new BehaviorSubject<any>(false);
    this.refreshStatusObservable = this.refreshStatusSubject.asObservable();
}


setRefreshStatus(status: boolean){
   this.refreshStatusSubject.next(status);
}


public get getSearchWord(): string {
    return this.searchSubject.value;
}

setSearchWord(word: string){
    this.searchSubject.next(word);
}

setFilterObject(filter: Filter){
    this.filterSubject.next(filter);
}

}