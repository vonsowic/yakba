import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ColumnFormSimpleComponent} from './column-form-simple.component';

describe('ColumnFormSimpleComponent', () => {
  let component: ColumnFormSimpleComponent;
  let fixture: ComponentFixture<ColumnFormSimpleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ColumnFormSimpleComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ColumnFormSimpleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
