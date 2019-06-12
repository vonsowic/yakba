import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CardFormSimpleComponent} from './card-form-simple.component';

describe('CardFormSimpleComponent', () => {
  let component: CardFormSimpleComponent;
  let fixture: ComponentFixture<CardFormSimpleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CardFormSimpleComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardFormSimpleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
