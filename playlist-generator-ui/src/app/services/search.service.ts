import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Filter } from '../models/Filter';


@Injectable({ providedIn: 'root' })
export class SearchService {

refreshStatus:string;
refreshStatusSubject: BehaviorSubject<any>;
refreshStatusObservable: Observable<any>;

placeholderValue:string;
placeholderValueSubject: BehaviorSubject<any>;
placeholderValueObservable: Observable<any>; 

searchValue:string;
searchValueSubject: BehaviorSubject<any>;
searchValueObservable: Observable<any>; 

word:string;
searchSubject: BehaviorSubject<any>;
searchWord: Observable<any>;

filter: Filter;
filterSubject: BehaviorSubject<any>;
filterObject: Observable<any>;

constructor(){
    
    this.placeholderValueSubject = new BehaviorSubject<any>(this.placeholderValue);
    this.placeholderValueObservable = this.placeholderValueSubject.asObservable();

    this.searchValueSubject = new BehaviorSubject<any>(this.searchValue);
    this.searchValueObservable = this.searchValueSubject.asObservable();

    this.searchSubject = new BehaviorSubject<any>(this.word);
    this.searchWord = this.searchSubject.asObservable();

    this.filterSubject = new BehaviorSubject<any>(this.filter);
    this.filterObject = this.filterSubject.asObservable();

    this.refreshStatusSubject = new BehaviorSubject<any>(false);
    this.refreshStatusObservable = this.refreshStatusSubject.asObservable();
}


setPlaceholderValue(value: string){
    this.placeholderValueSubject.next(value);
 }

setSearchValue(value: string){
   this.searchValueSubject.next(value);
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