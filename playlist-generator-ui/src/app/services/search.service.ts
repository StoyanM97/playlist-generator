import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Filter } from '../models/Filter';


@Injectable({ providedIn: 'root' })
export class SearchService {

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