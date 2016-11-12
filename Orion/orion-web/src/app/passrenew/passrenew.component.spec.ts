/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { PassrenewComponent } from './passrenew.component';

describe('PassrenewComponent', () => {
  let component: PassrenewComponent;
  let fixture: ComponentFixture<PassrenewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PassrenewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PassrenewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
