import { Injectable } from '@angular/core';

@Injectable()
export class PercentageService{
    private values: number[];

    constructor(){
        this.values = [];
        for(var i=0; i<=10; i++)  {
            
            this.values.push(i*10);
        }
    }

    public get getValues(): number[]{
       return this.values;
    }
}